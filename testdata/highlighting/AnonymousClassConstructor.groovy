import java.awt.event.ActionListener
import java.awt.event.ActionEvent

def x = new ActionListener(){
  def actionPerformed(ActionEvent e){

  }

  def <error descr="Constructors are not allowed in anonymous class">ActionListener</error>(){
    
  }
}