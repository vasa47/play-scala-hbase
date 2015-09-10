$ ->
    getBars()
   $("#addBar").submit (event) ->
    alert("after there")
    event.preventDefault()
    $.ajax
      url: event.target.action
      type: event.target.method
      contentType: "application/json"
      data: JSON.stringify({
        name: $("#carName").val(),
        fuel:$("#fuelType").val(),
        price:$("#price").val(),
        condition:$("#condition").val(),
        mileage:$("#mileage").val(),
        firstreg:$("#firstReg").val()
      })
      success: () ->
        getBars()
        $("#carName").val("")


    $("#editCar").submit (event) ->
      alert("we ll get here")
      event.preventDefault()
      $.ajax
        url: event.target.action
        type: event.target.method
        contentType: "application/json"
        data: JSON.stringify({

        })
        success: () ->
          getBars()
          $("#carName").val("")

getBars = () ->
  $.get "/bars", (bars) ->
    $("#bars").empty()
    $.each bars, (index, bar) ->
      $("#bars").append $("<li>").text bar.name



