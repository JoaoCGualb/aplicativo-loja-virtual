//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});

//Adicionar Produto
var tituloInput = document.getElementById('tituloInput');
var descricaoInput = document.getElementById('descricaoInput');
var precoInput = document.getElementById('precoInput');
var addButtonProduto = document.getElementById('addButtonProduto');
var radios = document.getElementsByName('tipo');
var uploader = document.getElementById('uploader');
var fileButton = document.getElementById('fileButton');
var link;

//Ao clicar no botao
addButtonProduto.addEventListener('click', function(){
       
        for (var i = 0; i < radios.length; i++) {
                if (radios[i].checked) {
                    var tipo = radios[i].value;
                }
            }

        if(tituloInput.value != ""){
                if(descricaoInput.value != ""){
                        if(precoInput.value != ""){
                                if (tipo != null && tipo !== undefined) {
                                        //if (link != null && link !== undefined) {
                                         create(tituloInput.value, descricaoInput.value,precoInput.value,tipo,link);
                                        //}else{
                                          //      alert("Imagem não carregado ou não selecionada");
                                        //}
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



function create(titulo, descricao,preco,tipo,link){
        
        var tituloPesquisa = titulo.toUpperCase();


        
        var newPostKey = firebase.database().ref().child('posts').push().key;

        var data = {
                titulo: titulo,
                tituloPesquisa:  tituloPesquisa,
                idProduto: newPostKey,      
                descricao: descricao,
                preco: parseFloat(preco),
                //imagem: link
        };
        firebase.database().ref("produtos/" + tipo + "/" + newPostKey).set(data);
        //firebase.database().ref("produtos/teste").set(data);

        alert("Produto Cadastrado");
        

}

//Ouvir evento change
fileButton.addEventListener('change', function(e){
        //obter o arquivo
        var file = e.target.files[0];

        //referenciar o storage
        var storageRef = firebase.storage().ref('imagens/produtos/' + file.name);

        //enviar o arquivo
        var task = storageRef.put(file);
        

        //atualizar o progress bar
        task.on('state_changed',
                function progress(snapshot){
                        var percent = (snapshot.bytesTransferred/ snapshot.totalBytes) * 100;
                        uploader.value = percent
                },
                function error(err){
                        console.log(err)
                },
                function complete(){
                        task.snapshot.ref.getDownloadURL().then(function(url){
                                link = url;
                        });
                        alert("Imagem enviado por completo!")                        
                }
                
                
                )
});