ui.mvc.newViewController = function(view, viewController){
	

	viewController.onLoad = function(){
		
		
		view.find("#adminFunctions").delegate("li","click",function(){
			
			var selItem = $(this);
			var target  = selItem.attr("navigateTo");
			
			
			ui.navi.navigateToTarget(target, { admin : true });
			
			
		});
		
	};
	
	viewController.onAppear = function(){
		
	};
	
};
