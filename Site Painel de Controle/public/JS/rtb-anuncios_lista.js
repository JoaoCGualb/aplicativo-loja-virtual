//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});
//Anuncios Lista
firebase.database().ref('anuncios').on('value', function(snapshot){
        snapshot.forEach(function(item){
                var tr = document.createElement('tr');
                var tdTitulo = document.createElement('td');
                var tdDescricao = document.createElement('td');
                var tdCarrosel = document.createElement('td');
                var tdImagem     = document.createElement('td');
                var linkImagem = document.createElement('a');

                
                var botaoDeletar = document.createElement('button');
                if(item.val().titulo != undefined){
                        tdTitulo.appendChild(document.createTextNode(item.val().titulo));
                }else{
                        tdTitulo.appendChild(document.createTextNode("Sem Titulo"));
   
                }
                if(item.val().descricao != undefined){
                        tdDescricao.appendChild(document.createTextNode(item.val().descricao));

                }else{
                        tdDescricao.appendChild(document.createTextNode("Sem Descrição"));
   
                }


                if(item.val().imagem != undefined){
                        linkImagem.appendChild(document.createTextNode(item.val().imagem));
                        linkImagem.href = item.val().imagem;
                        tdImagem.appendChild(linkImagem);
                }else{
                        tdImagem.appendChild(document.createTextNode("Sem Imagem"));
   
                }
                
                botaoDeletar.classList.add('teste');
                botaoDeletar.setAttribute('id', 'botaoDeletar');
                text = document.createTextNode('Deletar');
                botaoDeletar.appendChild(text);

                botaoDeletar.addEventListener('click',function(e){
                        firebase.database().ref('anuncios/' + item.key).remove();
                        window.location.reload();
                });
        

                tr.appendChild(tdTitulo);
                tr.appendChild(tdDescricao);
                tr.appendChild(tdImagem);
                tr.appendChild(botaoDeletar);


                document.getElementById('anunciosList').appendChild(tr);
        })
}, (error) => {
        if (error) {
          console.log(error.message)
        } else {
                console.log("Foi")
        }
      });

