function group_change(){
    console.log("Group changed")
}

function build_table(groups, categories) {
    
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
    tr.appendChild(frm)

    td = document.createElement('td');
    var input = document.createElement('input');
    input.setAttribute('list', 'groups')
    td.appendChild(input)
    var datalist = document.createElement('datalist');
    datalist.id = "groups"
    td.appendChild(datalist)
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

    tr.appendChild(td)
    td = document.createElement('td');
    td.appendChild(document.createTextNode("Amount"))
    tr.appendChild(td)
    td = document.createElement('td');
    td.appendChild(document.createTextNode("Amount"))
    tr.appendChild(td)
    tbdy.appendChild(tr);
    

    var tbl = document.createElement('table');  
    tbl.appendChild(tbdy);

    var article = document.getElementsByTagName('article')[0]
    article.appendChild(tbl)

    console.log(categories)
}