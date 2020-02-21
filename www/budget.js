function calc_sum() {
    var sum = 0;
    var table = document.getElementById('budget');
    for(let k = 0; k < table.rows.length; k++)
    {
        cat = document.getElementById("cat_" + k)
        if(cat != null)
        {
            // console.log("Adding: " + cat.value)
            sum += parseFloat(cat.value);
        }
    }

    document.getElementById('total').innerHTML = sum.toFixed(2);
  }
  window.onload=calc_sum;

  function value_change() {
    calc_sum()
    document.getElementById('add_btn').disabled = false;
  }
