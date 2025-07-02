/**
 * 
 */
 function getPrediction() {
    const region = $('#regionSelect').val();
    const years = $('#yearsInput').val();

    $.ajax({
    url: `http://localhost:8000/predict?years=${years}&region=${region}`,
    type: 'GET',
    dataType: 'text',   // JSON 대신 text로 받기
    success: function(data) {
        console.log(data);  // 문자열 그대로 출력됨
        $('#resultBox').html('<pre>' + data + '</pre>');  // JSON 문자열 그대로 출력
    },
    error: function(err) {
        console.error(err);
        alert('예측 요청 실패!');
    }
	});

}

$('#predictBtn').click(function() {
    getPrediction();
});

$('#regionSelect').change(function() {
    getPrediction();
});