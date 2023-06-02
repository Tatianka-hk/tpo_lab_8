document.getElementById('matrixForm').addEventListener('submit', function (e) {
  e.preventDefault();
  var formData = new FormData();
  formData.append('matrix1', document.getElementById('matrix1').files[0]);
  formData.append('matrix2', document.getElementById('matrix2').files[0]);
  var xhr = new XMLHttpRequest();
  var startTime = new Date().getTime();
  var requestCount = 10;
  var totalExecutionTime = 0;
  var completedRequests = 0;

  xhr.open('POST', '/multiply', true);
  xhr.responseType = 'text';

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var result = xhr.responseText;
      var formattedResult = JSON.parse(result);
      var table = createTable(formattedResult);
      var resultBlob = new Blob([table], { type: 'text/plain' });
      var resultURL = URL.createObjectURL(resultBlob);
      var resultLink = document.getElementById('resultLink');
      resultLink.href = resultURL;
      resultLink.download = 'result.txt';
      resultLink.style.display = 'block';
      var endTime = new Date().getTime();
      var executionTime = endTime - startTime;
      totalExecutionTime += executionTime;
      completedRequests++;

      console.log('Request', completedRequests);
      console.log('Client side:');
      console.log('Matrix size: 2500');
      console.log('Execution time:', executionTime + 'ms');

      if (completedRequests === requestCount) {
        var averageExecutionTime = totalExecutionTime / requestCount;
        console.log('Average Execution time:', averageExecutionTime + 'ms');
      }

      if (completedRequests < requestCount) {
        startTime = new Date().getTime();
        xhr.open('POST', '/multiply', true);
          xhr.responseType = 'text';
         xhr.send(formData);
      }
    }
  };

  xhr.send(formData);
});

document.getElementById('generateMatrixBtn').addEventListener('click', function (e) {
    e.preventDefault();
    var xhr = new XMLHttpRequest();
     var startTime = new Date().getTime();
      var requestCount = 10;
      var totalExecutionTime = 0;
      var completedRequests = 0;
    xhr.open('POST', '/serverMultiply', true);
       xhr.responseType = 'text';
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var result = xhr.responseText;
                            var formattedResult = JSON.parse(result);
                            var table = createTable(formattedResult);
                            var resultBlob = new Blob([table], { type: 'text/plain' });
                            var resultURL = URL.createObjectURL(resultBlob);
                            var resultLink = document.getElementById('resultLink');
                            resultLink.href = resultURL;
                            resultLink.download = 'result.txt';
                            resultLink.style.display = 'block';
                            var endTime = new Date().getTime();
                                  var executionTime = endTime - startTime;
                                  totalExecutionTime += executionTime;
                                  completedRequests++;


                             console.log('Request', completedRequests);
                                  console.log('Server side:');
                                  console.log('Matrix size: 1000');
                                  console.log('Execution time:', executionTime + 'ms');

                                  if (completedRequests === requestCount) {
                                    var averageExecutionTime = totalExecutionTime / requestCount;
                                    console.log('Average Execution time:', averageExecutionTime + 'ms');
                                  }

                                  if (completedRequests < requestCount) {
                                    startTime = new Date().getTime();
                                    xhr.open('POST', '/serverMultiply', true);
                                      xhr.responseType = 'text';
                                     xhr.send();
                                  }
        }
    };
    xhr.send();
});
function createTable(matrix) {
    var rows = matrix.length;
    var columns = matrix[0].length;

    var table = '';
    for (var i = 0; i < rows; i++) {
        for (var j = 0; j < columns; j++) {
            table += matrix[i][j];
            if (j < columns - 1) {
                table += '   ';
            }
        }
                table += '\n';
            }
    return  table ;
}