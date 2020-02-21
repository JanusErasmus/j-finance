function update() {
    var sum = 0;
    var table = document.getElementById('budget')
    for(let k = 0; k < table.rows.length; k++)
    {
        cat = document.getElementById("value_" + k)
        if(cat != null)
        {
            amount = cat.innerHTML

            if(amount < 0){
                cat_row = document.getElementById("cat_" + k)
                cat_row.style.backgroundColor = "lightsalmon" //#ffa07a
            }
            if(amount == 0){
                cat_row = document.getElementById("cat_" + k)
                cat_row.style.backgroundColor = "lightgreen" //#ffa07a
            }
            
        }
    }
  }
  window.onload=update;