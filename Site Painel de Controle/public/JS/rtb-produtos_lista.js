//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});
//Produtos Lista
buttonTaberna = document.getElementById("buttonTaberna");
buttonLoja = document.getElementById("buttonLoja");

buttonLoja.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
              
        firebase.database().ref('produtos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        var tr = document.createElement('tr');
                        var tdTitulo = document.createElement('td');
                        var tdDescricao = document.createElement('td');
                        var tdPreco = document.createElement('td');
                        var tdImagem     = document.createElement('td');
                        var linkImagem = document.createElement('a');
        
                        var botaoDeletar = document.createElement('button');
                        var botaoEditar = document.createElement('button');
        
                        tdTitulo.appendChild(document.createTextNode(item.val().titulo));
                        tdPreco.appendChild(document.createTextNode("R$ " + item.val().preco));
                        tdDescricao.appendChild(document.createTextNode(item.val().descricao));
                        linkImagem.appendChild(document.createTextNode(item.val().imagem));
                        tdImagem.appendChild(linkImagem);
                        botaoDeletar.classList.add('teste');
                        botaoDeletar.setAttribute('id', 'botaoDeletar');
                        text = document.createTextNode('Deletar');
                        botaoDeletar.appendChild(text);
                        botaoEditar.setAttribute('id', 'botaoDeletar');
                        textEditar = document.createTextNode('Editar');
                        botaoEditar.appendChild(textEditar);
        
        
                        botaoDeletar.addEventListener('click',function(e){
                                firebase.database().ref('produtos/loja/' + item.key).remove();
                                window.location.reload();
                        });
        
                        botaoEditar.addEventListener('click',function(e){
                                var dados = (item.key);
                                window.localStorage.setItem('id',dados);
                                window.localStorage.setItem('tipo','loja');
                                window.location.href = "editar_produto.html"
                        }); 
        
                        tr.appendChild(tdTitulo);
                        tr.appendChild(tdDescricao);
                        tr.appendChild(tdPreco);
                        
                        tr.appendChild(botaoDeletar);
                        tr.appendChild(botaoEditar);
        
        
        
                        document.getElementById('anunciosList').appendChild(tr);
                })
        })
});


buttonTaberna.addEventListener('click',function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }

        firebase.database().ref('produtos/taberna').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        var tr = document.createElement('tr');
                        var tdTitulo = document.createElement('td');
                        var tdDescricao = document.createElement('td');
                        var tdPreco = document.createElement('td');
                        var tdImagem     = document.createElement('td');
                        
                        var botaoDeletar = document.createElement('button');
                        var botaoEditar = document.createElement('button');

        
                        tdTitulo.appendChild(document.createTextNode(item.val().titulo));
                        tdPreco.appendChild(document.createTextNode("R$ " + item.val().preco));
                        tdDescricao.appendChild(document.createTextNode(item.val().descricao));
                        tdImagem.appendChild(document.createTextNode(item.val().imagem));
                        text = document.createTextNode('Deletar');
                        botaoDeletar.appendChild(text);
                        botaoEditar.setAttribute('id', 'botaoDeletar');
                        textEditar = document.createTextNode('Editar');
                        botaoEditar.appendChild(textEditar);
        
        
                        botaoDeletar.addEventListener('click',function(e){
                                firebase.database().ref('produtos/taberna/' + item.key).remove();
                                window.location.reload();
                        });
        
                        botaoEditar.addEventListener('click',function(e){
                                var dados = (item.key);
                                window.localStorage.setItem('id',dados);
                                window.localStorage.setItem('tipo','taberna');
                                window.location.href = "editar_produto.html"
                        }); 
        
                        tr.appendChild(tdTitulo);
                        tr.appendChild(tdDescricao);
                        tr.appendChild(tdPreco);
                        
                        tr.appendChild(botaoDeletar);
                        tr.appendChild(botaoEditar);
        
        
        
                        document.getElementById('anunciosList').appendChild(tr);
                })
        })
});
