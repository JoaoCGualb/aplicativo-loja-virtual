//Autenticacao
firebase.auth().onAuthStateChanged((user) => {
        if (!user) {
                window.location.href="index.html"
        }
});

//Pedidos taberna
firebase.database().ref('pedidos/taberna').on('value', function(snapshot){
        snapshot.forEach(function(item){
                var tr = document.createElement('tr');
                var tdCliente = document.createElement('td');
                var tdEndereco = document.createElement('td');
                var tdStatus     = document.createElement('td');
                var usuario = recuperarUsuario(item.val().idUsuario);
                

                tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                tdStatus.appendChild(document.createTextNode(item.val().status));
                //tdStatus.appendChild(document.createTextNode(usuario.nome + " " + usuario.sobrenome));
                
                

                var botaoStatus = document.createElement('button');
                var botaoImprimir = document.createElement('button');

                text = document.createTextNode('Alterar Status');
                botaoStatus.appendChild(text);

                botaoStatus.addEventListener('click',function(e){
                });

                tr.appendChild(tdCliente);
                tr.appendChild(tdEndereco);
                tr.appendChild(tdStatus);
                tr.appendChild(botaoStatus);
                tr.appendChild(botaoImprimir);

                document.getElementById('anunciosList').appendChild(tr);
        })
}, (error) => {
        if (error) {
          console.log(error.message)
        } else {
                console.log("Foi")
        }
      });


function recuperarUsuario(idUsuario){
        firebase.database().ref('usuarios').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().id == idUsuario){
                                
                                
                                var data = {
                                        email: item.val().email,
                                        id: item.val().id,
                                        nome:item.val().nome,
                                        sobrenome:item.val().sobrenome,
                                        telefone:item.val().telefone
                                }
                                return data;
                        }


                }
                )});
}
