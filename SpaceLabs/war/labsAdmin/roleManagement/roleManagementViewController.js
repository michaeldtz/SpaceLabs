ui.mvc.newViewController = function(view, viewController){
	
	var roleListArea	= view.find("#groupListArea");
	var roleDetailsArea	= view.find("#groupDetailsArea");
	var roleList;		
	
	var urlList			= "aapi/usermgmt/userrole/list";
	var urlDelete		= "aapi/usermgmt/userrole/delete";
	var urlCreate		= "aapi/usermgmt/userrole/create";
	
	/*
	 * On load of view and viewcontroller
	 */
	viewController.onLoad = function(){
		
		 roleList = new ui.controls.List(roleListArea, {
			 onSelect 	 : groupSelected,
			 icon		 : "../../resources/images/icons/group.png",
			 addFunction : addNewGroup,
			 delFunction : deleteProject
		 });
		 
		
	}; //end of onload
	
	viewController.onAppear = function(){
		roleDetailsArea.empty();
		reloadGroupList();		
	};	
	
	function addNewGroup(){
		var name = prompt("Please enter the name.");
		if(name !== undefined){
			ajax.post(urlCreate, { name : name }, function(result){
				reloadGroupList();
			});
		}
	}
	
	function deleteProject(object, item){
		var projectID = object.id;
		ajax.post(urlDelete, { id : projectID }, function(result){
			item.fadeOut();
		});
	}
	
	function groupSelected(object, index){
		roleDetailsArea.empty();
		
		for(var name in object){
			roleDetailsArea.append(name + ": " + object[name] + "<br/>");	
		}
		
		
	}	
	
	function reloadGroupList(){
		ajax.get(urlList,function(result){
			roleList.setListContent(result);
		});
	}
	
	function renderItem(item, object){
		item.empty();
		item.text(object.id + " | " + object.name);
		return item;
	}

	
	
};
