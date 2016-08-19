Backbone.Model.parse = function (resp, options) {
 	return resp.data;
};

Backbone.Collection.parse = function (resp, options) {
	return resp.data;
};

function showLocation(r){
    if(this.getStatus() == BMAP_STATUS_SUCCESS){//定位成功
        //新建中心点 并将地图中心移动过去
        var centerPoint = new BMap.Point(r.longitude,r.latitude);
        map.panTo(centerPoint);
        map.setCenter(centerPoint);
        //新建标注
        var mk = new BMap.Marker(centerPoint);
        mk.disableDragging();// 不可拖拽
        map.addOverlay(mk);
    }
    else {
        alert('failed: '+this.getStatus());//定位失败
    }        
}

$.fn.peity.defaults.line = {
	delimiter: ",",
	fill: "#c6d9fd",
	height: 16,
	max: null,
	min: 0,
	stroke: "#4d89f9",
	strokeWidth: 1,
	width: 48
}

$.noty.defaults = {
    layout           : 'topCenter',
    theme            : 'relax',
    type             : 'alert',
    text             : '', 
    dismissQueue     : true, 
    template         : '<div class="noty_message"><span class="noty_text"></span><div class="noty_close"></div></div>',
    animation        : {
        open: {
            height: 'toggle'
        },
        close: {
            height: 'toggle'
        },
        easing: 'swing',
        speed: 100
    },
    timeout          : 2000, 
    force            : false, 
    modal            : false,
    maxVisible       : 5, 
    killer           : false, 
    closeWith        : ['click'], 
    callback         : {
        onShow       : function() {},
        afterShow    : function() {},
        onClose      : function() {},
        afterClose   : function() {},
        onCloseClick : function() {},
    },
    buttons          : false
};

$.ajax({
	url: '/scripts/LoginView.js',
	dataType: "script",
	async: false
});

$.ajax({
	url: '/scripts/UserView.js',
	dataType: "script",
	async: false
});

$.ajax({
	url: '/scripts/QueryFormView.js',
	dataType: "script",
	async: false
});


$(function(){

	var AppRouter = Backbone.Router.extend({

	    initialize: function(){

	    	// $('#container').empty();
	    	// $('.navbar').remove();


	    },

	    execute: function(callback, args, name) {

	        if(Cookies.get('uid') == null){
				this.showLoginForm();
	            return false;
	        }

    		if (callback) callback.apply(this, args);
	    },

		routes : {
	        'user'			: 'showUser',
	        'login'			: 'showLoginForm',
	        'query'			: 'showQueryForm',
			
	        'logout'		: 'logout',

	        ''				: 'showUser',

	        //'*route'		: 'showLoginForm',
	    },

	    back: function(){
	        history.back();
	    },

	    showLoginForm: function(){

	    	$('.navbar').remove();
			
			$.get('/templates/Nav.html', function(data) {
				$('body').prepend(data);
			});
			
	        $('#container').empty();
			
			var loginView = new LoginView();
			$('#container').append(loginView.$el);
			
			loginView.render();

	    },

	    showUser: function(){

	    	var that = this;

	    	$('.navbar').remove();
			
			$.get('/templates/Nav.html', function(data) {
				$('body').prepend(data);
				$('#logout').click(function(){
		    		that.navigate("#logout", {trigger: true});
		    	});
			});

			$('#container').empty();

	        var user = new User({id : Cookies.get('uid')});
	        user.fetch({
	        	success: function(model, response, options){
	        		var userView = new UserView({ model : model });
					userView.render();
					$('#container').append(userView.$el);
	        	}
	        });
		
	    },

	    showQueryForm: function(){

	    	var that = this;

	    	$('.navbar').remove();
			
			$.get('/templates/Nav.html', function(data) {
				$('body').prepend(data);
				$('#logout').click(function(){
		    		that.navigate("#logout", {trigger: true});
		    	});
			});

			$('#container').empty();

			var view = new QueryFormView();

			$('#container').append(view.$el);

			view.render();
		
	    },

	    // 退出登录
	    logout: function(){

	    	$('.navbar').remove();

	        Cookies.remove('uid');

	        this.navigate('login', {trigger: true});
	    }
	});

	window.app = new AppRouter();
	Backbone.history.start({pushState: true, root: "/i/"});

});