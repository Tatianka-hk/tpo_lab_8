package lab8;
import org.springframework.beans.factory.annotation.Autowired;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
public class MyController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public MyController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @GetMapping(value = {"/", "/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody
    String getIndexHtml() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/index.html");
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }

    @GetMapping(value = "/script.js", produces = "application/javascript")
    public @ResponseBody
    String getScriptJs() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/script.js");
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }


    @PostMapping("/multiply")
    public int[][] multiplyMatrices(@RequestParam("matrix1") MultipartFile matrix1,
                                    @RequestParam("matrix2") MultipartFile matrix2) throws IOException {
        int[][] matrix1Data = readMatrixData(matrix1);
        int[][] matrix2Data = readMatrixData(matrix2);
        if (matrix1Data == null || matrix2Data == null) {
            throw new IllegalArgumentException("Invalid matrix data");
        }
        int [][] result =  MatrixMultiplier.multiplyMatrices(matrix1Data, matrix2Data);

        return result;
    }
    @PostMapping("/serverMultiply")
    public int[][]genData () throws IOException {
        String filePath1 = "C:\\Users\\Tatiana\\Desktop\\kpi2\\pto\\8 lab\\1000_1.txt";
        String filePath2 = "C:\\Users\\Tatiana\\Desktop\\kpi2\\pto\\8 lab\\1000_2.txt";
        int[][] matrix1Data = fromFile(filePath1);
        int[][] matrix2Data = fromFile(filePath2);
        int [][] result =  MatrixMultiplier.multiplyMatrices(matrix1Data, matrix2Data);

        return result;
    }
    private String getResourceContent(String resourceName) throws IOException {
        return Files.readString(Path.of("src/main/resources/static", resourceName));
    }

    public static int[][] fromFile(String filePath) throws FileNotFoundException {

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        int rows = 0;
        int columns = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            rows++;
            String[] values = line.split(" ");
            columns = values.length;
        }

        scanner.close();

        // Створюємо матрицю з визначеними розмірами
        int[][] matrix = new int[rows][columns];

        // Заново відкриваємо файл і заповнюємо матрицю
        scanner = new Scanner(file);

        int currentRow = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue; // Пропускаємо порожні рядки
            }

            String[] values = line.split(" ");
            for (int j = 0; j < columns; j++) {
                matrix[currentRow][j] = Integer.parseInt(values[j]);
            }

            currentRow++;
        }

        scanner.close();

        return matrix;
    }


    private int[][] readMatrixData(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        String fileContentString = new String(fileContent);

        String[] rows = fileContentString.trim().split("\n");
        int numRows = rows.length;
        int numCols = rows[0].trim().split("\\s+").length;

        int[][] matrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String[] elements = rows[i].trim().split("\\s+");

            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }

        return matrix;
    }

}