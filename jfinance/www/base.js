function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
}
  
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
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
