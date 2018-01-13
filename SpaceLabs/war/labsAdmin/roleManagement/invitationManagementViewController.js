ui.mvc.newViewController = function(view, viewController){
	
	var inviteListArea		= view.find("#inviteListArea");
	var inviteDetailsArea	= view.find("#inviteDetailsArea");
	var inviteList;		
	
	var urlList			= "aapi/invitation/list";
	var urlGet			= "aapi/invitation/get";
	var urlDelete		= "aapi/invitation/delete";
	var urlAccept		= "aapi/invitation/accept";
	
	/*
	 * On load of view and viewcontroller
	 */
	viewController.onLoad = function(){
		
		inviteList = new ui.controls.List(inviteListArea, {
			 onSelect 	 : invitationSelected,
			 icon		 : "../../resources/images/icons/group.png",
			 delFunction : deleteInvitation,
			 itemRender  : renderItem
		 });
		 
		
	}; //end of onload
	
	viewController.onAppear = function(){
		inviteDetailsArea.empty();
		reloadInvitationList();		
	};	
	
	
	function deleteInvitation(object, item){
		var projectID = object.id;
		ajax.post(urlDelete, { id : projectID }, function(result){
			item.fadeOut();
		});
	}
	
	function invitationSelected(object, index){
		inviteDetailsArea.empty();
		
		ajax.get(urlGet + "?id=" + object.id,function(result){
			
				
			for(var name in result){
				inviteDetailsArea.append(name + ": " + result[name] + "<br/>");	
			}
		
			$("<br/><button>Accept Invitation</button>").appendTo(inviteDetailsArea).click(function(){
				ajax.post(urlAccept, "id=" + object.id, function(result){
					inviteDetailsArea.empty();
					inviteDetailsArea.append("<div>New user created with id : " + result.newId + "</div>");
					reloadInvitationList();		
				});
			});
			
		});
		
	}	
	
	function reloadInvitationList(){
		ajax.get(urlList,function(result){
			inviteList.setListContent(result);
		});
	}
	
	function renderItem(item, object){
		item.empty();
		item.text(object.id + " | " + object.name);
		return item;
	}

	
	
};
