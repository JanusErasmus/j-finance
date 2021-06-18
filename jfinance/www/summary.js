$(document).ready(function(){     
    //override submit of the form
    // $("#categories_form").submit(function(e){

    //     e.preventDefault();
    //     $("#update_btn").prop('disabled', true);
    //     console.log("Update")

        
    //     let data = $("#categories_form").serializeArray()        
    //     data = parse_form(data)
    //     $.ajax({
    //         url: '/expenses',
    //         type: 'POST',
    //         data: JSON.stringify(data),
    
    //         success: function (jsonResponse) {
    //             let objresponse = JSON.parse(jsonResponse);
    //             console.log("Success")
    //             income = objresponse['income']
    //             expenses = parse_expenses_list(objresponse['expenses'])
    //             refresh_categories()
    
    //         },
    //         error: function () {
    //             $("#categories").text("Error to load api");
    //         }
    //     });
    // });

    var dataToSend = {};
    $.ajax({
        url: '/expenses',
        type: 'GET',
        data: dataToSend,

        success: function (jsonResponse) {
            let objresponse = JSON.parse(jsonResponse);
            income = objresponse['income']
            expenses = parse_expenses_list(objresponse['expenses'])
            console.log(expenses)

        },
        error: function () {
            $("#categories").text("Error to load api");
        }
    });
});