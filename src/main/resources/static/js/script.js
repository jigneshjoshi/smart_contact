console.log("Hello Base")
const toggleSidebar = () => {
    console.log("toggleSidebar");

    if ($(".sidebar").is(":visible")) {

        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");

    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");

    }
};

const search = () => {
    console.log("Searching......");
    let query = $("#search_input").val();

    if (query === ''){
        $(".search-bar").hide();

    }else {
        // search
        console.log(query);
        // sending request to the server
        let url = window.location.protocol + "//" + window.location.host +`/search/${query}`;
        // modern js used promise no need to use ajax
        fetch(url).then((response) =>{
            return response.json();
        } ).then((data)=>{
            // data
            console.log(data);
            // show data in html
            let text = `<div class='list-group'>`;
            data.forEach((contact)=>{
                text += `<a href="/user/${contact.cId}/contact" style="background: white; " class="list-group-item list-group-item-action">${contact.name}</a>`;
            });
            text += `</div>`
            // show search result using jquery
            $(".search-bar").html(text).show();

           // $(".search-result").show();
        });
        
        }
      
    
};
const paymentstart = () => {
    let payment = $("#inputField").val();
    console.log(payment);
    if(payment=="" || payment==null){
        alert("enter amount");
        return;
    }
    $.ajax(
        {
            url:'/user/create_order',
            data:JSON.stringify({amount:payment,info:"order_req"}),
            contentType:'application/json',
            type:'POST',
            dataType:'json',
            success:function(response){
                console.log(response)
                if(response.status == "created"){
                    //open pay page
                    let options={
                        key: "rzp_test_xDeywQjrteFN8q", // Enter the Key ID generated from the Dashboard
                        amount:response.amount , // Amount is in currency subunits. Default currency is
                       // INR. Hence, 50000 refers to 50000 paise
                        currency: "INR",
                        name: "jignesh",
                        description: "update plan",
                       // image: "https://example.com/your_logo",
                        order_id: response.order_id, //This is a sample Order ID. Pass the
                        //`id` obtained in the response of Step 1
                        handler: function (response){
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature)
                        console.log("successfully done ")
                        alert("congrates")
                        },
                        prefill: {
                            name: "",
                            email: "",
                            contact: ""
                            },
                            notes: {
                            address: "by jignesh "
                            
                            },
                            theme: {
                            color: "#3399cc"
                            }
                            };
                            var rzp1 = new Razorpay(options);
rzp1.on('payment.failed', function (response){
console.log(response.error.code);
console.log(response.error.description);
console.log(response.error.source);
console.log(response.error.step);
console.log(response.error.reason);
console.log(response.error.metadata.order_id);
console.log(response.error.metadata.payment_id);
alert("failed payment")
});
rzp1.open();
                
                }

            },
            error:function(error){
                console.log(error)
            }
            
        }
    )
        
    };

    