//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});

//Adicionar Anuncio Carrosel
var tituloInput = document.getElementById('tituloInput');
var descricaoInput = document.getElementById('descricaoInput');
var addButtonAnuncio = document.getElementById('addButtonAnuncio');
var uploader = document.getElementById('uploader');
var fileButton = document.getElementById('fileButton');
var link = ""
//Ao clicar no botao
addButtonAnuncio.addEventListener('click', function(){
        create(tituloInput.value, descricaoInput.value, link);
})

//Ouvir evento change
fileButton.addEventListener('change', function(e){
        //obter o arquivo
        var file = e.target.files[0];

        //referenciar o storage
        var storageRef = firebase.storage().ref('imagens/anuncios/' + file.name);

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
                        //alert("Envio completo!")                        
                }
                
                
                )
});



function create(titulo, descricao,imagem){
        var data = {
                titulo: titulo,
                descricao: descricao,
                carrosel: true,
                imagem: imagem
        };
        var newPostKey = firebase.database().ref().child('posts').push().key;

        firebase.database().ref('anuncios/' + newPostKey).set(data);
}

