  $( document ).ready(function() {
	  console.log("inside ready");
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
		                    data = '<a href="\/trade?user=test&name=' +data + '">' + data + '<\/a>';
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
	  });