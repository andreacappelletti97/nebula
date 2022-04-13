package GUI

import scala.swing._

object MainActivity:
  @main def runIt = {
    new Frame {
      title = "Nebula"


      val gridPanel = new GridPanel(5, 2) {
        contents += new Label("Actors:")

        val actorButton: Button = new Button("Select actors") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Actors Selected")
              val fileChooser = new FileChooser()
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              actorButton.text = path
              println(path)
          }
        }

        val messageButton: Button = new Button("Select messages") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Messages Selected")
              val fileChooser = new FileChooser()
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              messageButton.text = path
              println(path)
          }
        }

        val orchestratorButton: Button = new Button("Select orchestrator") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Orchestrator Selected")
              val fileChooser = new FileChooser()
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              orchestratorButton.text = path
              println(path)
          }
        }

        val startNebulaButton: Button = new Button("Start Nebula") {
          reactions += {
            case event.ButtonClicked(_) =>
              Nebula.Main.startNebula()

          }
        }

        val stopNebulaButton: Button = new Button("Stop Nebula") {
          reactions += {
            case event.ButtonClicked(_) =>
              Nebula.Main.stopNebula()

          }
        }

        contents += actorButton
        contents += new Label("Messages:")
        contents += messageButton
        contents += new Label("Orchestrator:")
        contents += orchestratorButton
        contents += new Label("Start:")
        contents += startNebulaButton
        contents += new Label("Stop:")
        contents += stopNebulaButton
      }
      contents = gridPanel
      size = new Dimension(700, 500)
      //pack()
      centerOnScreen()
      open()
    }
  }




