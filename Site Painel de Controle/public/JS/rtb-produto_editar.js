//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});

//Editar Produto
var tituloInput = document.getElementById('tituloInput');
var descricaoInput = document.getElementById('descricaoInput');
var precoInput = document.getElementById('precoInput');
var addButtonProduto = document.getElementById('addButtonProduto');
var uploader = document.getElementById('uploader');
var fileButton = document.getElementById('fileButton');
var link;
var idProduto;
var tipo;
var produto;
var encontrou;

idProduto = window.localStorage.getItem('id');
tipo = window.localStorage.getItem('tipo');

firebase.database().ref('produtos/' + tipo).on('value', function(snapshot){
        snapshot.forEach(function(item){
                if(item.val().idProduto == idProduto){
                        tituloInput.value =  (item.val().titulo);
                        descricaoInput.value = (item.val().descricao);
                        precoInput.value = (item.val().preco);
                        encontrou = true;
                        return;
                }
        })

        if(!encontrou){
                alert("Produto não encontrado");
        }
})


//Ao clicar no botao
addButtonProduto.addEventListener('click', function(){

        if(tituloInput.value != ""){
                if(descricaoInput.value != ""){
                        if(precoInput.value != ""){
                                if (tipo != null && tipo !== undefined) {
                                         create(tituloInput.value, descricaoInput.value,precoInput.value,tipo);
                                         window.history.back()
                                }else{
                                        alert("Seleciona a Loja");
                                }
                        }else{
                                alert("Insira um Preço");
                        }
                }else{
                        alert("Insira um Descrição");
                }
        }else{
                alert("Insira um titulo");
        }
        
/*

       for (var i = 0; i < radios.length; i++) {
           if (radios[i].checked) {
               var tipo = radios[i].value;
           }
       }
       create(tituloInput.value, descricaoInput.value,precoInput.value,tipo.value);*/
});



function create(titulo, descricao,preco,tipo){
        
        var tituloPesquisa = titulo.toUpperCase();


        
        var newPostKey = idProduto;

        var data = {
                titulo: titulo,
                tituloPesquisa:  tituloPesquisa,
                idProduto: newPostKey,      
                descricao: descricao,
                preco: parseFloat(preco),
        };

        firebase.database().ref('produtos/' + tipo + "/" + newPostKey).set(data);
        
        alert("Produto editado com sucesso!!")

        window.location.href = "produtos_lista.html"

}