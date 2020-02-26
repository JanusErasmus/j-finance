function get_row_element(name, amount) {

    var tr = document.createElement('tr');
    var td = document.createElement('td')
    td.appendChild(document.createTextNode(name))
    tr.appendChild(td)

    td = document.createElement('td')
    td.appendChild(document.createTextNode(amount))
    tr.appendChild(td)

    return tr
}

function show_table(id) {
    var el = document.getElementById(id)
    
    if(el.style.display == "none") {
        console.log("show")        
        el.style.display = ""
    } else {
        console.log("hide")        
        el.style.display = "none"
    }
}

function insert_row(tbdy, category) {
    var tr = document.createElement('tr');
    tr.style.backgroundColor = "white";

    if( category['type'] == "subcat") {
        //Insert category name
        var h_tr = get_row_element(category['name'], category['amount'])
        h_tr.style.backgroundColor = "lightgreen"
        h_tr.onclick = function() {
            var cell = this.getElementsByTagName("td")[0]
            var id = cell.innerHTML
            console.log("show " + id)
            show_table(id)
        }
        tbdy.appendChild(h_tr);
        
        //insert table of sub categories
        var sub_tbl = get_table(category['cats'])
        var td = document.createElement('td');
        td.colSpan = 2
        td.appendChild(sub_tbl)
        tr.id = category['name']
        tr.style.display = "none"
        tr.appendChild(td)
    } else {
        //insert category and name
        var td = document.createElement('td');
        td.appendChild(document.createTextNode(category['name']))
        tr.appendChild(td)

        td = document.createElement('td');
        td.appendChild(document.createTextNode(category['amount']))
        tr.appendChild(td)
    }

    tbdy.appendChild(tr);
}

function get_table(categories) {
    var tbl = document.createElement('table'); 
    var tbdy = document.createElement('tbody');


    for(var i = 0; i < categories.length; i++) {
        insert_row(tbdy, categories[i])
    }

    tbl.appendChild(tbdy);
    tbl.width = "100%"
    return tbl
}

function insert_table(tbdy, categories)
{
    for (var i = 0; i < categories.length; i++) {
        insert_row(tbdy, categories[i])
    }

    return tbdy
}

function build_table() {
    
    var table = []
    table.push({'name': "Sakgeld", 'amount':"1234", 'type': "cat"})
    table.push({'name': "Bier", 'amount':"14", 'type': "cat"})
    table.push({'name': "Spaar", 'amount':"1234", 'type': "cat"})
    table.push(
        {
        'name': "Noodsaaklik",
        'amount': 235,
        'type': "subcat",
        'cats':[{'name': "Losies", 'amount': 4},
                {'name': "Krag", 'amount': 5},
                {'name': "Bediende", 'amount': 6}
            ]
        }
    )
    table.push({'name': "Medies", 'amount':"1234", 'type': "cat"})
    table.push(
        {
        'name': "Voorsorg",
        'amount': 1235,
        'type': "subcat",
        'cats':[{'name': "Profmed", 'amount': 405},
                {'name': "PPS", 'amount': 5456},
                {'name': "OUTsurance",
                'amount': 601,
                'type': "subcat",
                'cats': [{'name': "Losies", 'amount': 4},
                {'name': "Losies", 'amount': 4},
                {'name': "Losies", 'amount': 4},
                {'name': "Losies", 'amount': 4},
                {'name': "Losies", 'amount': 4},
                {'name': "Losies_weer", 'amount': 4, 'type': 'subcat',
                'cats': [{
                    'name': "Voorsorg_weer",
                    'amount': 1235,
                    'type': "subcat",
                    'cats':[{'name': "Profmed", 'amount': 405},
                            {'name': "PPS", 'amount': 5456},
                            {'name': "OUTsurance_weer",
                            'amount': 601,
                            'type': "subcat",
                            'cats': [{'name': "Losies", 'amount': 4},
                            {'name': "Losies", 'amount': 4},
                            {'name': "Losies", 'amount': 4},
                            {'name': "Losies", 'amount': 4},
                            {'name': "Losies", 'amount': 4},
                            {'name': "Losies", 'amount': 4}
                        ]
                        }
                        ]
                    }]},
                    {'name': "Losies", 'amount': 4},
                    {'name': "Losies", 'amount': 4},
                    {'name': "Losies", 'amount': 4}]
                }
            
            
            
            ]
        }
    )
    
    //create table of summary  
    var tbdy = document.createElement('tbody');

    //insert heading
    var tr = document.createElement('tr');
    var td = document.createElement('th');
    td.appendChild(document.createTextNode("Category"))
    tr.appendChild(td)
    tbdy.appendChild(tr);

    td = document.createElement('th');
    td.appendChild(document.createTextNode("Amount"))
    tr.appendChild(td)
    tbdy.appendChild(tr);

    //insert rows
    insert_table(tbdy, table)
    
    var tbl = document.createElement('table');  
    tbl.appendChild(tbdy);

    var article = document.getElementsByTagName('article')[0]
    article.appendChild(tbl)
}


function update() {
    var sum = 0;

    build_table()
}

window.onload = update;