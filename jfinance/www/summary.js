function show_form(){
    $('#modal_window').show()
    $('#add_form').show()
}

function hide_form(){
    $('#modal_window').hide()
    $('#add_form').hide()
}

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
        $('#exp_id').val(expense.id)
        $('#exp_label').html(expense.label)
        show_form()
        $('#exp_label').show()
        $('#amount').val(expense.amount)

        if(expense.category === null){
            $('#desc').val("")
            $('#desc').show()
            $('#cat_label').hide()
            $('#desc').focus()
        } else {
            $('#desc').hide()
            $('#cat_label').html(expense.category.label)
            $('#cat_label').show()
            $('#amount').focus().select()
        }
    }
}

function get_expense_elment(id, expense){
    let html = ``
    let sum = 0
    let amount_class = ''

    if(expense.category === null)
    {
        if(expense.amount == 0){
            amount_class = "expense_null"
        }

        if(expense.amount < 0){
            amount_class = "expense_negative"
        }

        // console.log("Add ex:" + id)
        html += `<div class="expense expense_group" onclick="add_transaction(${id}, null)">
            <div class="cat_input">${expense.label}</div>
            <div class="cat_input ${amount_class}">${expense.amount.toLocaleString()}</div>
        </div>`
        sum = expense.amount
    } else {
        let sub_html = `<div class="expense_group">`
        for(let k = 0; k < expense.expenses.length; k++){
            amount_class = ''
            let sub_id = expense.expenses[k].id            
            sum += expense.expenses[k].amount

            if(expense.expenses[k].amount == 0){
                amount_class = "expense_null"
            }

            if(expense.expenses[k].amount < 0){
                amount_class = "expense_negative"
            }

            sub_html += `<div class="expense sub_expense" onclick="add_transaction(${id[1]}, ${sub_id})">
                            <div class="expense_bullet">•</div>
                            <div class="cat_input">${expense.expenses[k].label}</div>
                            <div class="cat_input ${amount_class}">${expense.expenses[k].amount.toLocaleString()}</div>
                        </div>`
        }
        sub_html += `</div>`

        html += `<div class="expense">
                    <div class="cat_input">${expense.label}</div>
                    <div class="cat_input" style="font-weight: bold;">${sum.toLocaleString()}</div>
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
    let amount_class = ''
    let keys = Object.keys(expenses)
    for(let k = 0; k < keys.length; k++){
        let id = keys[k]
        let e = get_expense_elment(id, expenses[id])
        sum += e.sum
        html += e.html
    }

    if(sum == 0){
        amount_class = "expense_null"
    }

    if(sum < 0){
        amount_class = "expense_negative"
    }

    html += `<div class="expense" style="font-weight:bold; border-top: thin solid black;">
        <div class="cat_input">TOTAL:</div>
        <div class="cat_input ${amount_class}" id="total">${sum.toLocaleString()}</div>
    </div>`

    $('#expenses').html(html)
}

$(document).ready(function(){
    $("#amount").on("click", function () {
        $(this).select();
     });

    var dataToSend = {'balance': true};
    $.ajax({
        url: '/expenses',
        type: 'GET',
        data: dataToSend,

        success: function (jsonResponse) {
            let objresponse = JSON.parse(jsonResponse);
            income = objresponse['income']
            expenses = parse_expenses_list(objresponse['expenses'])
            refresh_categories()
        },
        error: function () {
            $("#categories").text("Error to load api");
        }
    });
});