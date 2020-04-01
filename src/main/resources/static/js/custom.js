
function showShareDetails(data){
	sessionStorage.setItem("share", data);
	 window.location = "/trade";
	  } 

$( document ).ready(function() {
	  console.log("inside ready");
	  	if($('#dataTable').length >0 ){
		  $('#dataTable').DataTable({
		       "processing": true,
		        "serverSide": true,
		        "paging":   false,
		        "ordering": false,
		        "info":     true,
		        "searching": false,
			  "ajax": {
				  "url" : "/trade/all",
				  "dataSrc": ""
				  },
				  "columns" : [
		            { "data" : "name" },
		            { "data" : "symbol" ,"render": function(data, type, row, meta){
		                if(type === 'display'){
		                    data = '<a href=# onclick="return showShareDetails(\''+data+'\')">'+data+'<\/a>';
		                    
		                }

		                return data;
		             }},
		            { "data" : "mcap" },
		            { "data" : "price" },
		            { "data" : "open" },
		            { "data" : "prevClose" },
		            { "data" : "vol" },
		            { "data" : "eps" },
		            { "data" : "pe" },
		            { "data" : "bookVal" }
		        ]
		  });
	  	} 
	  	if($('#openCloseChart').length >0)  {
	  		 console.log("shares html share data "+sessionStorage.getItem("share"));
	  		 XHR.get("/trade/sharedetails?user=test&name="+sessionStorage.getItem("share"), {}, function(data) {
	  			console.log(data)
	  			stockChart(data.date,data.open,data.close,"Open","Close","openCloseChart")
	  			stockChart(data.date,data.high,data.low,"High","Low","highLowChart")
	  			drawVolumeChart(data.date,data.volume,"volumeChart")
	  			$("#shareName").html(data.name)
	  		 })
	  	}
	  });
