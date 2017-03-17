Backbone.Model.parse = function (resp, options) {
 	return resp.data;
};

Backbone.Collection.parse = function (resp, options) {
	return resp.data;
};

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


$.ajax({
	url: '/scripts/ResultView.js',
	dataType: "script",
	async: false
});

$.ajax({
	url: '/scripts/DoctorView.js',
	dataType: "script",
	async: false
});

$(function(){

	var AppRouter = Backbone.Router.extend({

	    initialize: function(){

	    	// $('#container').empty();
	    	// $('.navbar').remove();
	    	$('.navbar').remove();
			
			$.get('/templates/Nav.html', function(data) {
				$('body').prepend(data);
			});
			
	        $('#container').empty();

	    },

	    execute: function(callback, args, name) {

	    	$(document).unbind('scroll');

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
			'query/:section_id/:service/:preference/:log/:lat'	: 'showResult',
			'doctor/:id'	: 'showDoctor',

	        'logout'		: 'logout',

	        ''		: 'showUser',

	        //'*route'		: 'showLoginForm',
	    },

	    back: function(){
	        history.back();
	    },

	    showLoginForm: function(){

	    	$('#container').empty();

			var loginView = new LoginView();
			$('#container').append(loginView.$el);
			
			loginView.render();

	    },

	    showUser: function(){

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

	    	$('#container').empty();

			var view = new QueryFormView();

			$('#container').append(view.$el);

			view.render();
		
	    },

	    showResult: function(section_id, service, preference, log, lat){

	    	$('#container').empty();

	    	var view = new ResultView();

			$('#container').append(view.$el);

			view.render(section_id, service, preference, log, lat);

	    },

	    showDoctor: function(id) {
	    	$('#container').empty();

	        var doctor = new Doctor({id : id});
	        doctor.fetch({
	        	success: function(model, response, options){
	        		var view = new DoctorView({ model : model });
					view.render();
					$('#container').append(view.$el);
	        	}
	        });

	    },

	    // 退出登录
	    logout: function(){

	        Cookies.remove('uid');

	        this.navigate('login', {trigger: true});
	    }
	});

	window.app = new AppRouter();
	Backbone.history.start({pushState: true, root: "/i/"});

});