
/*---------------------------------------|TREE|------------------------------------------------*/
Tree = function(){
    var selectedElement;

    this.show = function(input){
        $('#trunk').html('');
        $.ajax({url : "/galerie/tree/",type : "GET", data:{folder:input}})
        .done(function(response){
            $('#trunk').append(response);
            $('#trunk').css('max-height',0.97*window.innerHeight);
            $(".my-folder").each(forEachFolder);
        });
    };

    var forEachFolder = function(){
       $(this).click(function(event){
            if(selectedElement) selectedElement.css('color','white');
            selectedElement = $(this);

            var selectedFolder = selectedElement.attr("path");
            thumbs.fetch(selectedFolder);

            $.ajax({url : "/galerie/tree/",type : "GET", data:{folder:selectedFolder}})
            .done(function(response){
                var isEmpty = $(response).attr('empty');
                if(isEmpty == 'true') return;
                selectedElement.unbind('click');
                selectedElement.css('color','darkgreen');
                selectedElement.children('ul').remove();
                selectedElement.append(response);
                selectedElement.children("ul").children("li").each(forEachFolder);
                selectedElement.click(forEachFolder);
            });
            event.stopPropagation();
       });
    };

    this.hide = function(){
      $('#trunk').html('');
    };
};
/*---------------------------------------|THUMBS|---------------------------------------------*/
Thumbs = function(){
    var count = 0;
    var current = 0;
    var fetchFlag = false;

    this.select = function(){
        if($('.thumb-select')) $('.thumb-select').click();
    };
    this.show = function(response){
        $('#thumbs').html(response);
    };
    this.pack = function(){
        $('#gallery').packery({itemSelector: '.thumb', gutter: 1,isResizeBound:true});
    };
    this.fetch = function(input){
        fetchFiles('/galerie/thumbs/',input);
    };
    this.search = function(input){
        fetchFiles('/galerie/search/',input);
    };

    var fetchFiles = function(url, input){
       navBar.notify('');
       mediaBox.destroy();
       imageBox.destroy();
       if(input.length == 0 || fetchFlag) {
           navBar.informNoInput();
           return;
       }
       $('#thumbs').html("<h2>Fetching files, please wait.</h2>");
       fetchFlag = true;
       $.ajax({url : url, type : "GET", data:{root:input}})
           .done(function(response){
               showThumbs(response);
               mediaBox.show();
               showFolderName(input);
           });
    };

    var showFolderName = function(input){
      var splits = '';
      if(input.indexOf("/") >= 0){
        splits = input.split("/");
        navBar.notify(splits[splits.length -1]);
      }else if (input.indexOf("\\") >= 0){
        splits = input.split("\\");
        navBar.notify(splits[splits.length -1]);
      } else{
        navBar.notify(input);
      }
    };

    var showThumbs = function(response){
        $('#thumbs').html(response);
        current = 0;
        count = $('.thumb').length;
        fetchFlag = false;

        $('.thumb').each(function(){
            $(this).click(function(){
                current = $(this).attr('index');
                imageBox.show($(this));
            });

            $(this).hover(
               function() {$(this).addClass("thumb-select");},
               function() {$(this).removeClass("thumb-select");}
            );
        });
        $('#gallery').imagesLoaded(function(){
            $('#gallery').packery({itemSelector: '.thumb', gutter: 1,isResizeBound:true});
        });
        $('.area').css('height',0.98*window.innerHeight);
    };
    this.selectOnRight = function(){
        if(current < count) current++;
        else current = 0;
        updateSelectedThumb();
    };
    this.selectOnLeft = function(){
        if(current > 0) current--;
        else current = count;
        updateSelectedThumb();
    };
    var updateSelectedThumb = function(){
        $('.thumb-select').removeClass('thumb-select');
        var selectedThumb = $($('.thumb')[current]);
        selectedThumb.addClass('thumb-select');
        imageBox.show(selectedThumb);
    };
};

/*----------------------------------------|MEDIA-BOX|-----------------------------------------------------*/
MediaBox = function(){
   this.show = function(){
      $('.file-link').click(function(){
          var path = $(this).attr('path');
          $.ajax({url : "/galerie/play/",type : "GET",data:{path:path}})
          .done(function(response){$('#player_msg').html(response);});
      });
      $('#player_msg').html('');
      this.dialog();
   };

   this.dialog = function(){
      if($("#media").length == 0) return;
      $("#media").dialog({
        autoOpen: true,
        closeOnEscape: true, draggable: true,
        width:0.9*$(window).width(), height:0.9*window.innerHeight,
        appendTo: "#thumbs",
        show: { effect: "slideDown", duration: 500 },
        hide: { effect: "slideUp", duration: 500 },
        position: { my: "right top", at: "right top", of: $('#thumbs')}
      });
      setTimeout(function() {
        $('#file-types').packery({itemSelector: '.file-type', gutter: 1,isResizeBound:true});
      }, 1500);
   };

   this.hide = function(){
        if($('#media').length !=0) {$('#media').dialog('close');}
   };
   this.destroy = function(){
        if($('#media').length !=0) {$('#media').dialog('destroy');}
   };
};

/*----------------------------------------|BOX|------------------------------------------------*/
var ImageBox = function(){
    this.boot = function(){
        this.hide();
        $('.box-img').click(this.hide);
        $('.box-lock').click(this.lockImage);
        $('.box-unlock').click(this.unlockImage);
        $('.box-max').click(this.maximise);
        $('.box-min').click(this.minimise);
        scroller.follow($('#box'));
    };

    this.show = function(selectedThumb){
        var img = selectedThumb.children('img');
        $('#box').show('slide',400);
        $('#box').attr('title',img.attr('description'));
        $('.box-img').attr('src',img.attr('orig'));
        $('.box-img').css('max-height',0.88*window.innerHeight);
    };

    this.hide = function(){
        $('#box').hide('slide',400);
    };
    this.destroy = function(){
        $('#box').hide('slide',400);
        $('.box-img').attr('src','');
    };
    this.maximise = function(){
        var width = $('#box').width();
        $('.box-img').css('max-height',0.97*document.height);
        $('#box').animate({width:width*1.1},50);
    };
    this.minimise = function(){
        var width = $('#box').width();
        $('#box').animate({width:width*0.9},50);
    };
    this.lockImage = function(){scroller.lock($('#box'));};
    this.unlockImage = function(){scroller.unlock($('#box'));};
};
/*----------------------------------------|NAVBAR|------------------------------------------------*/
var NavBar = function(){
     var helpHtml = '<table class="helper table table-condensed table-hover">'
        +'<tbody>'
        +'<tr><td class="help-col">f2</td><td>folder open, enter absolute folder paths</td></tr>'
        +'<tr><td class="help-col">f4</td><td>pack thumbnails to get compact mosaic layout</td></tr>'
        +'<tr><td class="help-col">f6</td><td>stop server</td></tr>'
        +'<tr><td class="help-col">f8</td><td>shortcut keys help</td></tr>'
        +'<tr><td class="help-col">+</td><td>lock image to stop scrolling</td></tr>'
        +'<tr><td class="help-col">-</td><td>unlock image to allow scrolling</td></tr>'
        +'<tr><td class="help-col"><</td><td>minimize viewing image</td></tr>'
        +'<tr><td class="help-col">></td><td>maximize viewing image</td></tr>'
        +'<tr><td class="help-col">esc</td><td>close all overlays</td></tr>'
        +'</tbody>'
        +'</table>'

    this.boot = function(){
        $('#root-txt').keypress(onTextKeyPress);
        $('#folder-btn').click(openFolder);
        $('#media-btn').click(showMediaBox);
        $('#pack-btn').click(packThumbs);
        $('#find-btn').click(this.searchInIndex);
        $('#index-btn').click(triggerIndex);
        $('#help-btn').popover({html:'true',content:helpHtml,placement:"bottom"});
        $('#stop-btn').click(this.stopServer);
        this.focus();
    };

    this.informNoInput = function(){
        $('#root-txt').attr("placeholder","input needed");
    };
    this.closeHelp = function(){
        if($('.helper').length !=0) $('#help-btn').click();
    };
    this.showHelp = function(){
        $('#help-btn').click();
    };
    this.focus = function(){
        $('#root-txt').focus();
    };
    var onTextKeyPress = function(event){
        if(event.keyCode == 13){
            openFolder();
            event.preventDefault();
        }
    };
    var openFolder = function(){
        imageBox.hide();
        tree.hide();
        thumbs.fetch($('#root-txt').val());
        tree.show($('#root-txt').val());
    };
    this.searchInIndex = function(){
        imageBox.hide();
        thumbs.search($('#root-txt').val());
    };
    var triggerIndex = function(){
        $.ajax({url : "/galerie/index/",type : "GET", data:{root:$('#root-txt').val()}})
         .done(function(response){$('#notifier').html(' '+response);});
    };
    var showMediaBox = function(){
        mediaBox.show();
    };
    var packThumbs = function(){
        thumbs.pack();
    };
    this.stopServer = function(){
        $.ajax({url : "/stop",type : "GET"}).done(function(response){thumbs.show(response)});
    };
    this.notify = function(msg){
        $('#notifier').html(' '+msg);
    };
};

/*---------------------------------------------|SHORTCUTS|------------------------------------------------*/
var ShortCuts = function(){
    this.boot = function(){
       window.addEventListener("keydown", setupKeys, false);
    };
    var setupKeys = function(e){
        switch(e.keyCode) {
            case 37:
                thumbs.selectOnLeft();
                break;
            case 39:
                thumbs.selectOnRight();
                break;
            case 188:
                imageBox.minimise();
                break;
            case 190:
                imageBox.maximise();
                break;
            case 27:
                homePage.closeAllOverlays();
                break;
            case 113:
                navBar.focus();
                break;
            case 115:
                thumbs.pack();
                break;
            case 117:
                homePage.closeAllOverlays();
                mediaBox.destroy();
                imageBox.destroy();
                tree.hide();
                navBar.stopServer();
                break;
            case 118:
                navBar.searchInIndex();
                break;
            case 119:
                navBar.showHelp()
                break;
            case 121:
                mediaBox.show();
                break;
            case 187:
                imageBox.lockImage();
                break;
            case 189:
                imageBox.unlockImage();
                break;
            case 16:
                thumbs.select();
                break;
        }
    };
};
/*----------------------------------------|SCROLLER|-----------------------------------------------------*/
Scroller = function(){
    var trackers = {};

    this.follow = function(target){
        trackers[target.attr('id')] = true;
        var target_pos_original = target.offset().top;
        $('.area').scroll(function(){
            if(!trackers[target.attr('id')])
                return;
            var target_pos = target.offset().top;
            var window_pos = $('.area').scrollTop();
            var final_pos = window_pos;
            if(window_pos < target_pos_original) {
                final_pos = target_pos_original;
                target.stop().css({'top':60});
            } else {
                target.stop().animate({'top':final_pos-target_pos_original+60},500);
            }
        });
    };
    this.lock= function(target){
        trackers[target.attr('id')] = false;
    };
    this.unlock= function(target){
        trackers[target.attr('id')] = true;
    };
};
/*----------------------------------------|HOMEPAGE|------------------------------------------------*/
HomePage = function(){
    this.closeAllOverlays = function(){
        navBar.closeHelp();
        mediaBox.hide();
        imageBox.hide();
    };
    this.boot = function(){
        navBar.boot();
        imageBox.boot();
        shortCuts.boot();
    };
};
/*---------------------------------------------|BOOT|------------------------------------------------*/
var tree = new Tree();
 var thumbs = new Thumbs();
 var navBar = new NavBar();
 var imageBox = new ImageBox();
 var mediaBox = new MediaBox();
 var shortCuts = new ShortCuts();
 var scroller = new Scroller();
 var homePage = new HomePage();

$(document).ready(function() {
    homePage.boot();
});

