console.log("this is script file")

const toggleSidebar=()=>{
	
	//if visible -> close bar
	if($('.sidebar').is(":visible")){
		
		//1.Dont show => disable it
		$(".sidebar").css("display","none");
		
		//2. margin-left was 20% , but now 0% => content will take the entire page
		$(".content").css("margin-left","0%");
	}
	//if close->visiable
	else{
		
		//1.opposite of if
		$(".sidebar").css("display","block");
		
		//2. opposite of if
		$(".content").css("margin-left","20%");
		
	}
};


const search = (userId)=>{
	//console.log("searching...");
	let query=$("#search-input").val();
	let id=userId;
	//if query blank don't show result
	if(query==''){
		
		$(".search-result").hide();
	}
	//if query present 
	else{
		//console.log(query);		
		//sending req to server
		
		let url = `http://localhost:8081/contsearch/${id}/${query}`;
		
		//using fetch api
		fetch(url).then(
			(response)=>{
				return response.json();
			}
		)
		.then(
			
			(data) => {
				console.log(data);
				
				let text=`<div class='list-group'>`;
				
				data.forEach(
					       (contact)=>{
						    text+=`<a href='/user/${id}/contact/${contact.cId}' class='list-group-item list-group-item-action'>${contact.name} </a>`
					        });
				
				            text+=`</div>`;
				            
				            $(".search-result").html(text);
				            $(".search-result").show();
			});
		
		 
	}
};