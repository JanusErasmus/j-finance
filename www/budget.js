function calc_sum() {
    console.log("Calculate total");

    var sum = 0;
    var table = document.getElementById('budget');
    for(let k = 1; k < table.rows.length; k++)
    {
        cat = document.getElementById("cat_" + k)
        if(cat != null)
        {
            sum += parseFloat(cat.value);
        }
    }

    document.getElementById('total').innerHTML = sum.toFixed(2);
  }
  window.onload=calc_sum;
