let expenses = null

function cat_checked(id){
    let checked = $(`#parent_${id}`).is(':checked');
    if(checked){
        console.log(`set ${id}`)
    }else{
        console.log(`clear ${id}`)
    }
}

function get_expense_elment(id, expense){
    let html = ``
    let sum = 0

    if(expense.expenses !== null) {
        let sub_html = `<div class="expense_group">`
        let keys = Object.keys(expense.expenses)
        for(let k = 0; k < keys.length; k++){
            let sub_id = keys[k]
            // console.log("Add sub ex:" + sub_id)
            sum += expense.expenses[sub_id].amount
            sub_html += `<div class="expense">
                <div class="expense_parent"></div>
                <div class="expense_bullet">•</div>
                <input type="text" class="cat_input" id="category_${id}_${sub_id}" name="category_${id}_${sub_id}" autocomplete="off" value="${expense.expenses[sub_id].label}"/>
                <input type="number" class="cat_input" id="amount_${id}_${sub_id}" name="amount_${id}_${sub_id}" autocomplete="off" value="${expense.expenses[sub_id].amount}"/>
            </div>`
        }
        sub_html += `<div class="expense">
                    <div class="expense_parent"></div>
                    <div class="expense_bullet">•</div>
                    <input type="text" class="cat_input" id="category_${id}_n" name="category_${id}_n" autocomplete="off" placeholder="Expense"/>
                    <input type="number" class="cat_input" id="amount_${id}_n" name="amount_${id}_n" autocomplete="off" placeholder="Amount"/>
                    </div>
                    </div>`

        html += `<div class="expense">
                <input type="checkbox" class="expense_parent" id="parent_${id}" name="parent_${id}" onClick="cat_checked(${id})" checked/>
                <input type="text" class="cat_input" id="category_${id}" name="category_${id}" autocomplete="off" value="${expense.label}"/>
                <div class="cat_input" id="amount_${id}">${sum}</div>
            </div>`
        html += sub_html
    } else {
        // console.log("Add ex:" + id)
        html += `<div class="expense expense_group">
            <input type="checkbox" class="expense_parent" id="parent_${id}" name="parent_${id}" onClick="cat_checked(${id})"/>
            <input type="text" class="cat_input" id="category_${id}" name="category_${id}" autocomplete="off" value="${expense.label}"/>
            <input type="number" class="cat_input" id="amount_${id}" name="amount_${id}" autocomplete="off" value="${expense.amount}"/>
        </div>`
        sum = expense.amount
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
    html += `<div class="expense expense_group">
                    <input type="checkbox" class="expense_parent" id="parent_n" name="parent_n" onClick="cat_checked(9999)"/>
                    <input type="text" class="cat_input" id="category_n" name="category_n" autocomplete="off" placeholder="Expense"/>
                    <input type="number" class="cat_input" id="amount_n" name="amount_n" autocomplete="off" placeholder="Amount"/>
                </div>`


    html += `<div class="expense">
        <div class="expense_parent"></div>
        <div class="cat_input"></div>
        <div class="cat_input" id="total">${sum}</div>
    </div>`
    $('#categories').html(html)

    $("#update_btn").prop('disabled', false);
}

function parse_form(data) {
    let form_expenses = {}
    for(let k = 0; k < data.length; k++){
        let line = data[k].name
        let args = line.split("_")
        if(args[0] === "amount") {
            if(args.length > 2){
                let id = args[1]
                let sub_id = args[2]
                if (typeof form_expenses[id] === 'undefined') {
                    form_expenses[id] = { expenses: {}}
                }
                if (typeof form_expenses[id].expenses === 'undefined') {
                    form_expenses[id].expenses = {}
                }
                if (typeof form_expenses[id].expenses[sub_id] === 'undefined') {
                    form_expenses[id].expenses[sub_id] = {}
                }
                form_expenses[id].expenses[sub_id].amount = data[k].value

            } else {
                let id = args[1]
                if (typeof form_expenses[id] === 'undefined') {
                    form_expenses[id] = {}
                }
                form_expenses[id].amount = data[k].value
            }
        }
        if(args[0] === "category") {
            if(args.length > 2){
                let id = args[1]
                let sub_id = args[2]
                if (typeof form_expenses[id] === 'undefined') {
                    form_expenses[id] = {}
                }
                if (typeof form_expenses[id].expenses === 'undefined') {
                    form_expenses[id].expenses = {}
                }
                if (typeof form_expenses[id].expenses[sub_id] === 'undefined') {
                    form_expenses[id].expenses[sub_id] = {}
                }
                form_expenses[id].expenses[sub_id].label = data[k].value
            } else {
                let id = args[1]
                if (typeof form_expenses[id] === 'undefined') {
                    form_expenses[id] = {}
                }
                form_expenses[id].label = data[k].value
            }
        }
    }

    return form_expenses
}

$(document).ready(function(){     
    //override submit of the form
    $("#categories_form").submit(function(e){

        e.preventDefault();
        $("#update_btn").prop('disabled', true);
        console.log("Update")

        
        let data = $("#categories_form").serializeArray()        
        data = parse_form(data)
        $.ajax({
            url: '/expenses',
            type: 'POST',
            data: JSON.stringify(data),
    
            success: function (jsonResponse) {
                let objresponse = JSON.parse(jsonResponse);
                console.log("Success")
                console.log(objresponse)
                refresh_categories()
    
            },
            error: function () {
                $("#categories").text("Error to load api");
            }
        });
    });

    var dataToSend = {poes:"simone" };
    $.ajax({
        url: '/expenses',
        type: 'GET',
        data: dataToSend,

        success: function (jsonResponse) {
            let objresponse = JSON.parse(jsonResponse);
            expenses = objresponse['expenses']
            refresh_categories()

        },
        error: function () {
            $("#categories").text("Error to load api");
        }
    });
});