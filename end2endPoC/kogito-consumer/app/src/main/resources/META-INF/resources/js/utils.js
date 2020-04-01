class DMNModel{
  constructor(DMNModel){
    this.DMNModel = DMNModel;
  }
}

class Decision{
  constructor(decision){
    this.decision = decision;
  }
}

function updateScroll(id){
    var element = document.getElementById(id);
    try {
        element.scrollTop = element.scrollHeight;
    }
    catch(err) {
    }

}