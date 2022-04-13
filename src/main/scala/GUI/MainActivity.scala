package GUI

import scala.swing._

object MainActivity:
  @main def runIt = {
    new Frame {
      title = "Nebula"

      contents = new FlowPanel {
        contents += new Label("Launch actors:")
        contents += new Button("Start") {
          reactions += {
            case event.ButtonClicked(_) =>
              Nebula.Main.nebulaMain()
          }
        }
        contents += new Button("Stop") {
          reactions += {
            case event.ButtonClicked(_) =>
              Nebula.Main.stopNebula()
          }
        }
      }

      pack()
      centerOnScreen()
      open()
    }
  }




