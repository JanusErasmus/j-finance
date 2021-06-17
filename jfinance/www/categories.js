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

    if(expense.category === null)
    {
        // console.log("Add ex:" + id)
        html += `<div class="expense expense_group">
            <input type="text" class="cat_input" id="category_${id}" name="category_${id}" autocomplete="off" value="${expense.label}"/>
            <input type="number" class="cat_input" id="amount_${id}" name="amount_${id}" autocomplete="off" value="${expense.amount}"/>
        </div>`
        sum = expense.amount
    } else {
        let sub_html = `<div class="expense_group">`
        for(let k = 0; k < expense.expenses.length; k++){
            let sub_id = expense.expenses[k].id
            sum += expense.expenses[k].amount
            sub_html += `<div class="expense">
                <div class="expense_bullet">•</div>
                <input type="text" class="cat_input" id="category_${id}_${sub_id}" name="category_${id}_${sub_id}" autocomplete="off" value="${expense.expenses[k].label}"/>
                <input type="number" class="cat_input" id="amount_${id}_${sub_id}" name="amount_${id}_${sub_id}" autocomplete="off" value="${expense.expenses[k].amount}"/>
            </div>`
        }
        sub_html += `<div class="expense">
                    <div class="expense_bullet">•</div>
                    <input type="text" class="cat_input" id="category_${id}_n" name="category_${id}_n" autocomplete="off" placeholder="Expense"/>
                    <input type="number" class="cat_input" id="amount_${id}_n" name="amount_${id}_n" autocomplete="off" placeholder="Amount"/>
                    </div>
                    </div>`

        html += `<div class="expense">
                <input type="text" class="cat_input" id="category_${id}" name="category_${id}" autocomplete="off" value="${expense.label}"/>
                <div class="cat_input" id="amount_${id}">${sum}</div>
            </div>`
        html += sub_html
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

function parse_expenses_list(exp_list){
    let exp = {}
    for(let k = 0; k < exp_list.length; k++){
        if(exp_list[k].category === null){
            exp[exp_list[k].id] = exp_list[k]
        } else {
            let cat_key = 'c' + exp_list[k].category.id
            if (typeof exp[cat_key] === 'undefined') {
                exp[cat_key] = {'label': exp_list[k].category.label, 'expenses': []}
            }
            exp[cat_key].expenses.push(exp_list[k])
        }
    }

    return exp
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
                expenses = parse_expenses_list(objresponse['expenses'])
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
            expenses = parse_expenses_list(objresponse['expenses'])
            refresh_categories()

        },
        error: function () {
            $("#categories").text("Error to load api");
        }
    });
});