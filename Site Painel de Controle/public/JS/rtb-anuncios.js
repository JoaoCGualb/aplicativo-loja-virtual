//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});

//Adicionar Anuncio
var tituloInput = document.getElementById('tituloInput');
var descricaoInput = document.getElementById('descricaoInput');
var addButtonAnuncio = document.getElementById('addButtonAnuncio');


//Ao clicar no botao
addButtonAnuncio.addEventListener('click', function(){
        create(tituloInput.value, descricaoInput.value);
})

function create(titulo, descricao){
        firebase.database().ref('anuncios').push({
                        titulo: titulo,
                        descricao: descricao,
                        carrosel: false
                });
}

