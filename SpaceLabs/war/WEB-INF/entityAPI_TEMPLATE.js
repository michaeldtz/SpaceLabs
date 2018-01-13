var entityAPI = entityAPI || {};

entityAPI.#ENTITYNAME# = {
		
/*put*/ put : function(dataObj, callback){ 
/*put*/    $.ajax({ type		: "POST", contentType : "application/json",  
/*put*/			   	url 		: "#APIURL#?action=put", data: JSON.stringify(dataObj), 
/*put*/			   	success 	: function(newID){ if(typeof callback == "function") callback(newID, entityAPI.#ENTITYNAME#); }
/*put*/    });
/*put*/ },

/*update*/ update : function(dataObj, callback){ 	
/*update*/    $.ajax({ 	type		: "POST", contentType : "application/json",  
/*update*/			   	url 		: "#APIURL#?action=update", data: JSON.stringify(dataObj), 
/*update*/			   	success 	: function(result){ if(typeof callback == "function") callback(result, entityAPI.#ENTITYNAME#); }
/*update*/    });
/*update*/ },

/*delete*/ del : function(id, callback){ 	
/*delete*/    $.post("#APIURL#?action=delete&id="+id,function(result){ if(typeof callback == "function") callback(result, entityAPI.#ENTITYNAME#); });
/*delete*/ },
	
/*get*/ get : function(id, callback){ 	
/*get*/    $.get("#APIURL#?action=get&id="+id, function(data){ 
/*get*/			entityAPI.#ENTITYNAME#.determineAssociations(data)
/*get*/			if(typeof callback == "function") 
/*get*/				callback(data, entityAPI.#ENTITYNAME#); 
/*get*/			});
/*get*/ },

/*query*/ query : function(criteria, value, callback){ 	
/*query*/   $.get("#APIURL#?action=query&criteria="+criteria + "&value=" + value, function(list){ 
/*query*/		entityAPI.#ENTITYNAME#.determineAssociations(list);
/*query*/		if(typeof callback == "function") 
/*query*/			callback(list, entityAPI.#ENTITYNAME#); 
/*query*/   });
/*query*/ },

/*queryAll*/ queryAll : function(callback){ 	
/*queryAll*/ 	$.get("#APIURL#?action=queryAll", function(list){ 
/*queryAll*/		entityAPI.#ENTITYNAME#.determineAssociations(list);
/*queryAll*/		if(typeof callback == "function") 
/*queryAll*/			callback(list, entityAPI.#ENTITYNAME#); 
/*queryAll*/    });
/*queryAll*/ },


/*getFX*/ getFX : function(id,fieldname,callback){ 	
/*getFX*/    $.get("#APIURL#?action=getFX&id=" + id +"&fxentity=" +fieldname, function(data){ 
/*getFX*/		entityAPI.#ENTITYNAME#.determineAssociations(data);			
/*getFX*/		if(typeof callback == "function") 
/*getFX*/			callback(data, entityAPI.#ENTITYNAME#); 
/*getFX*/		});
/*getFX*/ },
/*getFX*/ queryReverseFX : function(id,fieldname,callback){ 	
/*getFX*/    $.get("#APIURL#?action=queryReverseFX&id=" + id +"&fxentity=" +entity + "&fxfield=" + fieldname, function(list){ 
/*getFX*/			entityAPI.#ENTITYNAME#.determineAssociations(list);
/*getFX*/			if(typeof callback == "function") 
/*getFX*/				callback(list, entityAPI.#ENTITYNAME#); 
/*getFX*/			});
/*getFX*/ },


		getFXFunctionFor : function(entity, id){
			return function(callback){
				entityAPI[entity].get(id,callback);
			}
		},
		getReverseFXFunctionFor : function(entity,fieldname,id){
				return function(callback){
					entityAPI[entity].query(fieldname,id,callback);
			}
		},




		determineAssociations : function(objOrArray){
			var list = undefined;
			if(objOrArray.length != undefined)
				list = objOrArray
			else
				list = [objOrArray];
			
			for(var i = 0; i < list.length; i++){
				var fxEnt     = #FX_ENTITIES#;
				
				for(var j = 0; j < fxEnt.length; j++){
					var fieldName = fxEnt[j].field;
					var fxEntity  = fxEnt[j].entity;
					var foreignID = list[i][fieldName];
					if(entityAPI.#ENTITYNAME#.getFXFunctionFor != undefined)
						list[i]["get" + fieldName] =  entityAPI.#ENTITYNAME#.getFXFunctionFor(fxEntity, foreignID) ;
				}
		
				var revFxEnt = #REVERSE_FX_ENTITIES#;	
				for(var j = 0; j < revFxEnt.length; j++){
					var fieldName = revFxEnt[j].field;
					var fxEntity  = revFxEnt[j].entity;
					var fxName  = revFxEnt[j].name;
					var foreignID = list[i].id;
					if(entityAPI.#ENTITYNAME#.getReverseFXFunctionFor != undefined)
						list[i]["query" + fxName] = entityAPI.#ENTITYNAME#.getReverseFXFunctionFor(fxEntity, fieldName, foreignID);
				}
			}
		}


		
}

