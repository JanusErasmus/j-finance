var instance_categories = []

function update_cats(){
    var selected = document.getElementById("selected_group")
    var current_group = selected.value

    var datalist = document.getElementById("cats")
    datalist.textContent = ' '

    //insert categories drop down    
    for (var i = 0; i < instance_categories.length; i++) {

        if(current_group === instance_categories[i]['group']) {
            var opt = document.createElement('option');
            opt.value = instance_categories[i]['name'];
            datalist.appendChild(opt)
        }
    }
}

function group_change(){
    update_cats()
}

function build_table(groups, categories) {
    
    // Keep global reference to categories
    instance_categories = categories

    //create table of summary  
    var tbdy = document.createElement('tbody');

    //insert heading
    var tr = document.createElement('tr');
    var td = document.createElement('th');
    td.appendChild(document.createTextNode("Group"))
    tr.appendChild(td)
    tbdy.appendChild(tr);
    
    td = document.createElement('th');
    td.appendChild(document.createTextNode("Category"))
    tr.appendChild(td)
    tbdy.appendChild(tr);

    td = document.createElement('th');
    td.appendChild(document.createTextNode("Amount"))
    tr.appendChild(td)
    tbdy.appendChild(tr);

    var frm = document.createElement('form');
    frm.method = "POST"
    tr = document.createElement('tr');

    td = document.createElement('td');
    var input = document.createElement('input');
    input.autocomplete = 'off'
    input.name = "group"
    input.id = "selected_group"
    input.setAttribute('list', 'groups')
    td.appendChild(input)
    var datalist = document.createElement('datalist');
    datalist.id = "groups"
    td.appendChild(datalist)
    tr.appendChild(td)

    //insert group drop down    
    for (var i = 0; i < groups.length; i++) {
        
        //Add Group        
        var opt = document.createElement('option');
        opt.value = groups[i]['name'];
        datalist.appendChild(opt)
        
        // //Add Cat
        // td = document.createElement('td');
        // td.appendChild(document.createTextNode(categories[i]['name']))
        // tr.appendChild(td)
        // tbdy.appendChild(tr);
    }
    input.onchange = group_change

    td = document.createElement('td');
    input = document.createElement('input');
    input.autocomplete = 'off'
    input.name = "category"
    input.setAttribute('list', 'cats')
    td.appendChild(input)    
    datalist = document.createElement('datalist');
    datalist.id = "cats"
    td.appendChild(datalist)
    tr.appendChild(td)

    td = document.createElement('td');
    input = document.createElement('input');
    input.autocomplete = 'off'
    input.name = "amount"
    input.id = "amount"
    td.appendChild(input)
    tr.appendChild(td)
    
    var button = document.createElement('input');
    button.type = 'submit'
    tr.appendChild(button)

    
    tbdy.appendChild(tr);
    
    
    var tbl = document.createElement('table');  
    tbl.appendChild(tbdy);
    frm.appendChild(tbl)

    var article = document.getElementsByTagName('article')[0]
    article.appendChild(frm)
}