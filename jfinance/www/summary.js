function add_transaction(id, sub_id){
    let expense = null

    if(sub_id === null){
        expense = expenses[id]
    } else {
        id = 'c' + id
        let exp = expenses[id]        
        for(let k = 0; k < exp.expenses.length; k++){
            if(sub_id == exp.expenses[k].id){
                expense = exp.expenses[k]
                break;
            }
        }
    }
    
    if(expense != null){
        console.log(expense)
    }
}

function get_expense_elment(id, expense){
    let html = ``
    let sum = 0

    if(expense.category === null)
    {
        // console.log("Add ex:" + id)
        html += `<div class="expense expense_group" onClick="add_transaction(${id}, null)">
            <div class="cat_input">${expense.label}</div>
            <div class="cat_input">${expense.amount}</div>
        </div>`
        sum = expense.amount
    } else {
        let sub_html = `<div class="expense_group">`
        for(let k = 0; k < expense.expenses.length; k++){
            let sub_id = expense.expenses[k].id
            sum += expense.expenses[k].amount
            sub_html += `<div class="expense sub_expense" onClick="add_transaction(${id[1]}, ${sub_id})">
                            <div class="expense_bullet">•</div>
                            <div class="cat_input">${expense.expenses[k].label}</div>
                            <div class="cat_input">${expense.expenses[k].amount}</div>
                        </div>`
        }
        sub_html += `</div>`

        html += `<div class="expense">
                    <div class="cat_input">${expense.label}</div>
                    <div class="cat_input">${sum}</div>
                </div>`
                
        html += sub_html
        html += `</div>`
    }

    return {html: html, sum: sum};
}

function refresh_categories(){
    // console.log(expenses)
    let sum = 0
    let html = ``
    let keys = Object.keys(expenses)
    for(let k = 0; k < keys.length; k++){
        let id = keys[k]
        let e = get_expense_elment(id, expenses[id])
        sum += e.sum
        html += e.html
    }

    html += `<div class="expense" style="font-weight:bold; border-top: thin solid black;">
        <div class="cat_input">TOTAL:</div>
        <div class="cat_input" id="total">${sum}</div>
    </div>`

    $('#expenses').html(html)
}

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
            refresh_categories()
        },
        error: function () {
            $("#categories").text("Error to load api");
        }
    });
});