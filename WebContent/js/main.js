// The root URL for the RESTful services
var rootURL = "http://localhost:8080/JM/rest/products";

var currentproducts;

// Recupera lista dos produtos quando a aplicação inicia	
findAll();

// Botão delete escondido no estado inicial
$('#btnDelete').hide();

// Registrar listeners
$('#btnSearch').click(function() {
	search($('#searchKey').val());
	return false;
});

// Trigger para o botão enter
$('#searchKey').keypress(function(e){
	if(e.which == 13) {
		search($('#searchKey').val());
		e.preventDefault();
		return false;
    }
});

$('#btnAdd').click(function() {
	newproducts();
	return false;
});

$('#btnSave').click(function() {
	if ($('#productseId').val() == '')
		addproducts();
	else
		updateproducts();
	return false;
});

$('#btnDelete').click(function() {
	deleteproducts();
	return false;
});

$('#productsList a').live('click', function() {
	findById($(this).data('identity'));
});

//Substitui imagem com erro , por imagem genérica
$("img").error(function(){
  $(this).attr("src", "pics/products.png");

});

function search(searchKey) {
	if (searchKey == '') 
		findAll();
	else
		findByName(searchKey);
}

function newproducts() {
	$('#btnDelete').hide();
	currentproducts = {};
	renderDetails(currentproducts); // Display empty form
}

function findAll() {
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json", // data type of response
		success: renderList
	});
}

function findByName(searchKey) {
	console.log('findByName: ' + searchKey);
	$.ajax({
		type: 'GET',
		url: rootURL + '/search/' + searchKey,
		dataType: "json",
		success: renderList 
	});
}

function findById(id) {
	console.log('findById: ' + id);
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id,
		dataType: "json",
		success: function(data){
			$('#btnDelete').show();
			console.log('findById success: ' + data.name);
			currentproducts = data;
			renderDetails(currentproducts);
		}
	});
}

function addproducts() {
	console.log('addproducts');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('products created successfully');
			$('#btnDelete').show();
			$('#productseId').val(data.id);
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addproducts error: ' + textStatus);
		}
	});
}

function updateproducts() {
	console.log('updateproducts');
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: rootURL + '/' + $('#productseId').val(),
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('products updated successfully');
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('updateproducts error: ' + textStatus);
		}
	});
}

function deleteproducts() {
	console.log('deleteproducts');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/' + $('#productseId').val(),
		success: function(data, textStatus, jqXHR){
			alert('products deleted successfully');
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('deleteproducts error');
		}
	});
}

function renderList(data) {
	// JAX-RS serializa uma lista vazia como nulo, e uma "coleção de one 'como um objeto (nao um' matriz de um ')
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$('#productsList li').remove();
	$.each(list, function(index, products) {
		$('#productsList').append('<li><a href="#" data-identity="' + products.id + '">'+products.name+'</a></li>');
	});
}

function renderDetails(products) {
	$('#productseId').val(products.id);
	$('#name').val(products.name);
	$('#description').val(products.description);
	$('#price').val(products.price);
	$('#category').val(products.category);
	$('#pic').attr('src', 'pics/' + products.picture);
	
}

//Função auxiliar para serializar todos os campos do formulário em uma string JSON
function formToJSON() {
	var productseId = $('#productseId').val();
	return JSON.stringify({
		"id": productseId == "" ? null : productseId, 
		"name": $('#name').val(), 
		"description": $('#description').val(),
		"price": $('#price').val(),
		"category": $('#category').val(),
		"picture": currentproducts.picture
		});
}
