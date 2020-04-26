
function build_table(categories) {
    //create table of summary  
    var tbdy = document.createElement('tbody');

    //insert heading
    var tr = document.createElement('tr');
    var td = document.createElement('th');
    td.appendChild(document.createTextNode("Group"))
    tr.appendChild(td)
    td = document.createElement('th');
    td.appendChild(document.createTextNode("Category"))
    tr.appendChild(td)
     td = document.createElement('th');
    td.appendChild(document.createTextNode("Amount"))
    tr.appendChild(td)
    tbdy.appendChild(tr);

    //insert rows
    for (let k = 0; k < categories.length; k++){
        tr = document.createElement('tr');
        td = document.createElement('td');
        td.appendChild(document.createTextNode(categories[k]['group']))
        tr.appendChild(td)
        td = document.createElement('td');
        td.appendChild(document.createTextNode(categories[k]['name']))
        tr.appendChild(td)
        td = document.createElement('td');
        td.appendChild(document.createTextNode(categories[k]['sum']))
        tr.appendChild(td)
        tbdy.appendChild(tr);
    }
    
    var tbl = document.createElement('table');  
    tbl.appendChild(tbdy);

    var article = document.getElementsByTagName('article')[0]
    article.appendChild(tbl)
}

