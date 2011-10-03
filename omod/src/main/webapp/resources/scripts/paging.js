
function changePageSize( baseLink )
{
	var pageSize = jQuery("#sizeOfPage").val();
	if(  !/^ *[0-9]+ *$/.test(pageSize) ){
		alert("Invalid number!");
	}else {
		window.location.href = baseLink +"pageSize=" + pageSize ;
	}
}

function jumpPage( baseLink )
{
	var pageSize = jQuery("#sizeOfPage").val();
	var currentPage = jQuery("#jumpToPage").val();
	if(  !/^ *[0-9]+ *$/.test(pageSize)  ||  !/^ *[0-9]+ *$/.test(currentPage) ){
		alert("Invalid number!");
	}else {
		window.location.href = baseLink +"pageSize=" + pageSize +"&currentPage=" +currentPage;
	}
}