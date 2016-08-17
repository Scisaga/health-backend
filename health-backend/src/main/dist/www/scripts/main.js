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
    layout           : 'bottom',
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
	url: 'scripts/LoginView.js',
	dataType: "script",
	async: false
});

$.ajax({
	url: 'scripts/LoginView.js',
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
	        ''				: 'showUser',
	        'pick'			: 'showNodes',
	        'login'			: 'showLoginForm',
			
	        'logout'		: 'logout',

	        '*route'		: 'back',
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
			loginView.render();
			
			console.log(loginView.$el);
			$('#container').append(loginView.$el);

	        //var loginView = new LoginView();
			//loginView.render();
			//$('#container').append(loginView.$el);
	    },

	    showUser: function(){

	    	$('.navbar').remove();
	    	$('body').prepend(tpl.get('Nav.html'));

	        var user = new User({id : Cookies.get('uid')});
	        user.fetch({
	        	success: function(model, response, options){
	        		var userView = new UserView({ model : model });
					userView.render();
					$('.navbar-header').append(userView.$el);
	        	}
	        });

	    	$('#container').empty();
		
	    },

	    // 退出登录
	    logout: function(){

	    	$('.navbar').remove();

	        Cookies.remove('uid');
	    }
	});

	window.app = new AppRouter();
	Backbone.history.start({pushState: true, root: "index.html"});

});