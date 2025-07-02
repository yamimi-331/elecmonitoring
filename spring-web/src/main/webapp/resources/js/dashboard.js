/**
 * 
 */
 function getPrediction() {
    const region = $('#regionSelect').val();
    const years = $('#yearsInput').val();

    $.ajax({
    url: `http://localhost:8000/predict?years=${years}&region=${region}`,
    type: 'GET',
    dataType: 'json',
    success: function(data) {
        console.log(data);
        $('#resultBox').html('<pre>' + JSON.stringify(data, null, 2) + '</pre>');
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