//Autenticacao
var deslogado = document.getElementById("deslogado");
var logado = document.getElementById("logado");
var buttonLogar = document.getElementById("buttonLogar");
var buttonCadastrar = document.getElementById("buttonCadastrar");
var floatingPassword = document.getElementById("floatingPassword");
var floatingInput = document.getElementById("floatingInput");

buttonCadastrar.addEventListener('click',function(){
        firebase.auth().createUserWithEmailAndPassword(floatingInput.value, floatingPassword.value)
        .then((user) => {
                
                validarUsuario(user.uid);

        })
        .catch((error) => {
        var text = "";
        switch(error){
                case "auth/email-already-in-use":
                        text = "Email já cadastado!"
                break;
                case "auth/invalid-email":
                        text = "Email inválido!"
                break;
                case"auth/weak-password":
                        text = "Insira uma senha mais forte!"
                break;
        }
                alert(text)
        });
})


buttonLogar.addEventListener('click',function(){
        firebase.auth().signInWithEmailAndPassword(floatingInput.value, floatingPassword.value)
        .then((user) => {
        })
        .catch((error) => {
                var text = "";
                switch(error){
                        case "auth/user-not-found":
                                text = "Usuario não cadastrado!"
                        break;
                        case "auth/invalid-email":
                                text = "Email inválido!"
                        break;
                        case"auth/wrong-password":
                                text = "Senha Inválida!"
                        break;
                }
                        alert(text)
                logado.style.display="none"
                deslogado.style.display="block"
              });
})

  

firebase.auth().onAuthStateChanged((user) => {
        if (user) {
                console.log(" 1:  "+user.uid)
                validarUsuario(user.uid);
        } else {
        logado.style.display="none"
        deslogado.style.display="block"
        }
});

function validarUsuario(id){
        if(id == "QvOI9hF0PIYekx7I2r3Ay5h04YD2"){
                console.log("2:  "+id)
                logado.style.display="block"
                deslogado.style.display="none"
        }else{
                alert("Usuario não autorizado!! Entre em contato com o Suporte");
                firebase.auth().signOut().then(() => {
                        // Sign-out successful.
                      }).catch((error) => {
                        // An error happened.
                      });
        }
}

//Pedidos Loja
var btnTodos = document.getElementById("buttonTodos");
var btnAguardando = document.getElementById("buttonAguardando");
var btnAViagem = document.getElementById("buttonACaminho");
var btnConfirmados = document.getElementById("buttonConfirmados");
var btnFinalizados = document.getElementById("buttonFinalizados");
var btnCancelados = document.getElementById("buttonCancelados");

var usuario;
var idUsuario;
var idPedido;
var telefone;
total = 0;


btnTodos.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        var tr = document.createElement('tr');
                        var tdCliente = document.createElement('td');
                        var tdEndereco = document.createElement('td');
                        var tdStatus     = document.createElement('td');
                        
                        firebase.database().ref('usuarios').on('value', function(snapshot){
                                snapshot.forEach(function(usuarios){
                                        if(usuarios.key == item.val().idUsuario){   
                                                telefone = usuarios.val().telefone;   
                                                usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                tdCliente.appendChild(document.createTextNode(usuario));
                                        }
                
                
                                }
                                )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));
                
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus);
                        var botaoStatus = document.createElement('button');
                        var botaoImprimir = document.createElement('button');
                        text = document.createTextNode('Alterar Status');
                        botaoStatus.appendChild(text);
                        
                        botaoStatus.addEventListener('click',function(e){
                            alert("Solicitamos que vá para a respectiva aba do status atual do pedido para altera-la,  grato!")
                    });
                       
                    var imprimir = document.createTextNode('Imprimir Pedido');
                    botaoImprimir.appendChild(imprimir);

                    botaoImprimir.addEventListener('click',function(e){
                        var doc = new jsPDF('p','pt','letter'    )
        
                        doc.setFontSize(16);
                        doc.text("Pedido", 35, 25);
                        doc.text("Cliente: " + usuario, 35, 50);
                        doc.text("Endereço: " + item.val().endereco, 35, 75);
                        doc.text("Telefone: " + telefone, 35, 100);
                        doc.text("Observações: " + item.val().observacao, 35, 125);
        
                              
                              
                              function createHeaders(keys) {
                                var result = [];
                                var width;
                                for (var i = 0; i < keys.length; i += 1) {
                                switch(i){
                                        case 0: 
                                        width = 50;
                                        break;
                                        case 1: 
                                        width=600;
                                        break;
                                        case 2: 
                                        width=90;
                                        break;
                                        case 3: 
                                        width=50;
                                        break;        
                                }
                                        
                                  result.push({
                                    id: keys[i],
                                    name: keys[i],
                                    prompt: keys[i],
                                    width: width,
                                    align: "center",
                                    padding: 0
                                  });
                                }
                                return result;
                              }
                              
                              var headers = createHeaders([
                                '#',
                                'Nome Produto',
                                'Preço',
                                'Qtd'
                              ]);
                              
                        var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                            doc.table(10, 200, generateData(), headers, { autoSize: false  });
                        doc.save( usuario + '-' + item.val().data + '.pdf')
                   });
            
                
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
});

btnAguardando.addEventListener("click", function(){
       limparTabela();
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().status == "aguardando"){
                        idPedido = item.val().idPedido;
                       
                        idUsuario = item.val().idUsuario;
                       
                        var tr = document.createElement('tr');
                        var tdCliente = document.createElement('td');
                        var tdEndereco = document.createElement('td');
                        var tdStatus     = document.createElement('td');
                        
                        firebase.database().ref('usuarios').on('value', function(snapshot){
                                snapshot.forEach(function(usuarios){
                                        if(usuarios.key == item.val().idUsuario){   
                                                telefone = usuarios.val().telefone;   
                                                usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                tdCliente.appendChild(document.createTextNode(usuario));
                                        }
                
                
                                }
                                )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));                      
                        
        
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus);
                                var botaoStatus = document.createElement('button');
        var botaoImprimir = document.createElement('button');
        text = document.createTextNode('Alterar Status');
        botaoStatus.appendChild(text);
        imprimir = document.createTextNode('Imprimir Pedido');
        botaoImprimir.appendChild(imprimir);
        
        botaoStatus.addEventListener('click',function(e){
                switch(item.val().status){
                        case "aguardando":
                                atualizarStatus("confirmado");
                                break;
                        case "a_caminho":
                            atualizarStatus("finalizado");
                                break;
                        case "confirmado":
                            atualizarStatus("a_caminho");
                                break;
                }
        });
       
        botaoImprimir.addEventListener('click',function(e){
                var doc = new jsPDF('p','pt','letter'    )

                doc.setFontSize(16);
                doc.text("Pedido", 35, 25);
                doc.text("Cliente: " + usuario, 35, 50);
                doc.text("Endereço: " + item.val().endereco, 35, 75);
                doc.text("Telefone: " + telefone, 35, 100);
                doc.text("Observações: " + item.val().observacao, 35, 125);

                      
                      
                      function createHeaders(keys) {
                        var result = [];
                        var width;
                        for (var i = 0; i < keys.length; i += 1) {
                        switch(i){
                                case 0: 
                                width = 50;
                                break;
                                case 1: 
                                width=600;
                                break;
                                case 2: 
                                width=90;
                                break;
                                case 3: 
                                width=50;
                                break;        
                        }
                                
                          result.push({
                            id: keys[i],
                            name: keys[i],
                            prompt: keys[i],
                            width: width,
                            align: "center",
                            padding: 0
                          });
                        }
                        return result;
                      }
                      
                      var headers = createHeaders([
                        '#',
                        'Nome Produto',
                        'Preço',
                        'Qtd'
                      ]);
                      
                  var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                doc.text("Valor Total: R$" + total, 400, 100);
                doc.table(10, 200, generateData(), headers, { autoSize: false  });
                doc.save( usuario + '-' + item.val().data + '.pdf')
           });
    

       tr.appendChild(botaoStatus);
       tr.appendChild(botaoImprimir);
        
                        document.getElementById('anunciosList').appendChild(tr);
                }
                })
        }, (error) => {
                if (error) {
                  console.log(error.message)
                } else {
                        console.log("Foi")
                }
              });        
});

btnAViagem.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().status == "a_caminho"){
                        idPedido = item.val().idPedido;
                        total = 0;

                        idUsuario = item.val().idUsuario;
                        var tr = document.createElement('tr');
                        var tdCliente = document.createElement('td');
                        var tdEndereco = document.createElement('td');
                        var tdStatus     = document.createElement('td');
                        
                        firebase.database().ref('usuarios').on('value', function(snapshot){
                                snapshot.forEach(function(usuarios){
                                        if(usuarios.key == item.val().idUsuario){   
                                                telefone = usuarios.val().telefone;   
                                                usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                tdCliente.appendChild(document.createTextNode(usuario));
                                        }
                
                
                                }
                                )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));
        
                        
        
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus); 
                                var botaoStatus = document.createElement('button');
        var botaoImprimir = document.createElement('button');
        text = document.createTextNode('Alterar Status');
        botaoStatus.appendChild(text);
        imprimir = document.createTextNode('Imprimir Pedido');
        botaoImprimir.appendChild(imprimir);

    
        botaoStatus.addEventListener('click',function(e){
                switch(item.val().status){
                        case "aguardando":
                                atualizarStatus("confirmando");
                                break;
                        case "a_caminho":
                            atualizarStatus("finalizado");
                                break;
                        case "confirmado":
                            atualizarStatus("a_caminho");
                                break;
                }
        });
       
        botaoImprimir.addEventListener('click',function(e){
                var doc = new jsPDF('p','pt','letter'    )

                doc.setFontSize(16);
                doc.text("Pedido", 35, 25);
                doc.text("Cliente: " + usuario, 35, 50);
                doc.text("Endereço: " + item.val().endereco, 35, 75);
                doc.text("Telefone: " + telefone, 35, 100);
                doc.text("Observações: " + item.val().observacao, 35, 125);

                      
                      
                      function createHeaders(keys) {
                        var result = [];
                        var width;
                        for (var i = 0; i < keys.length; i += 1) {
                        switch(i){
                                case 0: 
                                width = 50;
                                break;
                                case 1: 
                                width=600;
                                break;
                                case 2: 
                                width=90;
                                break;
                                case 3: 
                                width=50;
                                break;        
                        }
                                
                          result.push({
                            id: keys[i],
                            name: keys[i],
                            prompt: keys[i],
                            width: width,
                            align: "center",
                            padding: 0
                          });
                        }
                        return result;
                      }
                      
                      var headers = createHeaders([
                        '#',
                        'Nome Produto',
                        'Preço',
                        'Qtd'
                      ]);
                      
                  var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                doc.text("Valor Total: R$" + total, 400, 100);

                    doc.table(10, 200, generateData(), headers, { autoSize: false  });
                doc.save( usuario + '-' + item.val().data + '.pdf')
           });
    

       tr.appendChild(botaoStatus);
       tr.appendChild(botaoImprimir);                    
        
                        document.getElementById('anunciosList').appendChild(tr);
                }
                })
        }, (error) => {
                if (error) {
                  console.log(error.message)
                } else {
                        console.log("Foi")
                }
              });        
});
btnConfirmados.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().status == "confirmado"){
                        idPedido = item.val().idPedido;
                        total = 0;

                        idUsuario = item.val().idUsuario;
                        var tr = document.createElement('tr');
                        var tdCliente = document.createElement('td');
                        var tdEndereco = document.createElement('td');
                        var tdStatus     = document.createElement('td');
                        
                        firebase.database().ref('usuarios').on('value', function(snapshot){
                                snapshot.forEach(function(usuarios){
                                        if(usuarios.key == item.val().idUsuario){   
                                                telefone = usuarios.val().telefone;   
                                                usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                tdCliente.appendChild(document.createTextNode(usuario));
                                        }
                
                
                                }
                                )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));
        
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus);
                                var botaoStatus = document.createElement('button');
        var botaoImprimir = document.createElement('button');
        text = document.createTextNode('Alterar Status');
        botaoStatus.appendChild(text);
        imprimir = document.createTextNode('Imprimir Pedido');
        botaoImprimir.appendChild(imprimir);

    
        botaoStatus.addEventListener('click',function(e){
                switch(item.val().status){
                        case "aguardando":
                                atualizarStatus("confirmando");
                                break;
                        case "a_caminho":
                            atualizarStatus("finalizado");
                                break;
                        case "confirmado":
                            atualizarStatus("a_caminho");
                                break;
                }
        });
       
        botaoImprimir.addEventListener('click',function(e){
                var doc = new jsPDF('p','pt','letter'    )

                doc.setFontSize(16);
                doc.text("Pedido", 35, 25);
                doc.text("Cliente: " + usuario, 35, 50);
                doc.text("Endereço: " + item.val().endereco, 35, 75);
                doc.text("Telefone: " + telefone, 35, 100);
                doc.text("Observações: " + item.val().observacao, 35, 125);

                      
                      
                      function createHeaders(keys) {
                        var result = [];
                        var width;
                        for (var i = 0; i < keys.length; i += 1) {
                        switch(i){
                                case 0: 
                                width = 50;
                                break;
                                case 1: 
                                width=600;
                                break;
                                case 2: 
                                width=90;
                                break;
                                case 3: 
                                width=50;
                                break;        
                        }
                                
                          result.push({
                            id: keys[i],
                            name: keys[i],
                            prompt: keys[i],
                            width: width,
                            align: "center",
                            padding: 0
                          });
                        }
                        return result;
                      }
                      
                      var headers = createHeaders([
                        '#',
                        'Nome Produto',
                        'Preço',
                        'Qtd'
                      ]);
                      
                  var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                doc.text("Valor Total: R$" + total, 400, 100);

                    doc.table(10, 200, generateData(), headers, { autoSize: false  });
                doc.save( usuario + '-' + item.val().data + '.pdf')
           });
    

       tr.appendChild(botaoStatus);
       tr.appendChild(botaoImprimir);
        
                        document.getElementById('anunciosList').appendChild(tr);
                }
                })
        }, (error) => {
                if (error) {
                  console.log(error.message)
                } else {
                        console.log("Foi")
                }
              });        
});

btnCancelados.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().status == "cancelado"){
                        idPedido = item.val().idPedido;
                        total = 0;

                        idUsuario = item.val().idUsuario;
                        var tr = document.createElement('tr');
                        var tdCliente = document.createElement('td');
                        var tdEndereco = document.createElement('td');
                        var tdStatus     = document.createElement('td');
                        
                        firebase.database().ref('usuarios').on('value', function(snapshot){
                                snapshot.forEach(function(usuarios){
                                        if(usuarios.key == item.val().idUsuario){   
                                                telefone = usuarios.val().telefone;   
                                                usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                tdCliente.appendChild(document.createTextNode(usuario));
                                        }
                
                
                                }
                                )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));                        

        
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus);
                                var botaoStatus = document.createElement('button');
        var botaoImprimir = document.createElement('button');
        text = document.createTextNode('Alterar Status');
        botaoStatus.appendChild(text);
        imprimir = document.createTextNode('Imprimir Pedido');
        botaoImprimir.appendChild(imprimir);

    
        botaoStatus.addEventListener('click',function(e){
            alert("Solicitamos que vá para a respectiva aba do status atual do pedido para altera-la,  grato!")
    });
       
        botaoImprimir.addEventListener('click',function(e){
                var doc = new jsPDF('p','pt','letter'    )

                doc.setFontSize(16);
                doc.text("Pedido", 35, 25);
                doc.text("Cliente: " + usuario, 35, 50);
                doc.text("Endereço: " + item.val().endereco, 35, 75);
                doc.text("Telefone: " + telefone, 35, 100);
                doc.text("Observações: " + item.val().observacao, 35, 125);

                      
                      
                      function createHeaders(keys) {
                        var result = [];
                        var width;
                        for (var i = 0; i < keys.length; i += 1) {
                        switch(i){
                                case 0: 
                                width = 50;
                                break;
                                case 1: 
                                width=600;
                                break;
                                case 2: 
                                width=90;
                                break;
                                case 3: 
                                width=50;
                                break;        
                        }
                                
                          result.push({
                            id: keys[i],
                            name: keys[i],
                            prompt: keys[i],
                            width: width,
                            align: "center",
                            padding: 0
                          });
                        }
                        return result;
                      }
                      
                      var headers = createHeaders([
                        '#',
                        'Nome Produto',
                        'Preço',
                        'Qtd'
                      ]);
                      
                  var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                doc.text("Valor Total: R$" + total, 400, 100);

                    doc.table(10, 200, generateData(), headers, { autoSize: false  });
                doc.save( usuario + '-' + item.val().data + '.pdf')
           });
    

       tr.appendChild(botaoStatus);
       tr.appendChild(botaoImprimir);
        
                        document.getElementById('anunciosList').appendChild(tr);
                }
                })
        }, (error) => {
                if (error) {
                  console.log(error.message)
                } else {
                        console.log("Foi")
                }
              });        
});

btnFinalizados.addEventListener("click", function(){
        while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
        firebase.database().ref('pedidos/loja').on('value', function(snapshot){
                snapshot.forEach(function(item){
                        if(item.val().status == "finalizado"){
                                idPedido = item.val().idPedido;
                        total = 0;

                                 idUsuario = item.val().idUsuario;
                                 var tr = document.createElement('tr');
                                 var tdCliente = document.createElement('td');
                                 var tdEndereco = document.createElement('td');
                                 var tdStatus     = document.createElement('td');
                                 
                                 firebase.database().ref('usuarios').on('value', function(snapshot){
                                         snapshot.forEach(function(usuarios){
                                                 if(usuarios.key == item.val().idUsuario){      
                                                         usuario = usuarios.val().nome + " " + usuarios.val().sobrenome;
                                                         tdCliente.appendChild(document.createTextNode(usuario));
                                                 }
                         
                         
                                         }
                                         )});
                        
                        tdEndereco.appendChild(document.createTextNode(item.val().endereco));
                        tdStatus.appendChild(document.createTextNode(item.val().status));
                        
                        
        
                        tr.appendChild(tdCliente);
                        tr.appendChild(tdEndereco);
                        tr.appendChild(tdStatus);                        
                                var botaoStatus = document.createElement('button');
        var botaoImprimir = document.createElement('button');
        text = document.createTextNode('Alterar Status');
        botaoStatus.appendChild(text);
        imprimir = document.createTextNode('Imprimir Pedido');
        botaoImprimir.appendChild(imprimir);

    
        botaoStatus.addEventListener('click',function(e){
            alert("Solicitamos que vá para a respectiva aba do status atual do pedido para altera-la,  grato!")
    });
       
        botaoImprimir.addEventListener('click',function(e){
                var doc = new jsPDF('p','pt','letter'    )

                doc.setFontSize(16);
                doc.text("Pedido", 35, 25);
                doc.text("Cliente: " + usuario, 35, 50);
                doc.text("Endereço: " + item.val().endereco, 35, 75);
                doc.text("Telefone: " + telefone, 35, 100);
                doc.text("Observações: " + item.val().observacao, 35, 125);

                      
                      
                      function createHeaders(keys) {
                        var result = [];
                        var width;
                        for (var i = 0; i < keys.length; i += 1) {
                        switch(i){
                                case 0: 
                                width = 50;
                                break;
                                case 1: 
                                width=600;
                                break;
                                case 2: 
                                width=90;
                                break;
                                case 3: 
                                width=50;
                                break;        
                        }
                                
                          result.push({
                            id: keys[i],
                            name: keys[i],
                            prompt: keys[i],
                            width: width,
                            align: "center",
                            padding: 0
                          });
                        }
                        return result;
                      }
                      
                      var headers = createHeaders([
                        '#',
                        'Nome Produto',
                        'Preço',
                        'Qtd'
                      ]);
                      
                  var generateData = function() {
                                var result = [];
                                total = 0;
                                item.val().itens.forEach(function(produto){
                                var produtoJson = JSON.parse(JSON.stringify(produto));
                                var contador = 1;
                                 var data = {
                                "#": contador,
                                "Nome Produto": produtoJson.nomeProduto.toString() ,
                                "Preço": "R$ " +  produtoJson.preco ,
                                "Qtd": parseInt(produtoJson.quantidade)
                                }
                                
                                total = total + parseFloat(produtoJson.preco) * parseInt(produtoJson.quantidade)
                                result.push(Object.assign({}, data));
                                contador = contador + 1;
                        });
                        doc.text("Valor Total: R$" + total, 400, 100);
                        return result;
                      };

                doc.text("Valor Total: R$" + total, 400, 100);

                    doc.table(10, 200, generateData(), headers, { autoSize: false  });
                doc.save( usuario + '-' + item.val().data + '.pdf')
           });
    

       tr.appendChild(botaoStatus);
       tr.appendChild(botaoImprimir);

                        document.getElementById('anunciosList').appendChild(tr);
                }
                })
        }, (error) => {
                if (error) {
                  console.log(error.message)
                } else {
                        console.log("Foi")
                }
              });        
});

function atualizarStatus(status){
    var updates = {};

    updates['/pedidos/loja/' + idPedido + "/status"] = status;
    updates['/meus_pedidos/'+ idUsuario +'/loja/' + idPedido + "/status"] = status;
  
    firebase.database().ref().update(updates);
}

function limparTabela(){
     while (document.getElementById("anunciosList").hasChildNodes()) {  
                document.getElementById("anunciosList").removeChild(document.getElementById("anunciosList").firstChild);
              }
}