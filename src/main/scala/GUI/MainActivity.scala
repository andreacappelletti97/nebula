package GUI

import java.io.File
import scala.swing.*

object MainActivity:

  // Mutable variables for input of the User
  var actorJsonPath = ""
  var messagesJsonPath = ""
  var orchestratorJsonPath = ""
  var monitoringJsonPath = ""
  var clusteringJsonPath = ""
  var systemRunning = false

  @main def runIt = {
    new Frame {
      title = "Nebula"


      val gridPanel: GridPanel = new GridPanel(7, 2) {
        contents += new Label("Actors:")

        val actorButton: Button = new Button("Select actors") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Actors Selected")
              val fileChooser = new FileChooser(new File("/Users/andreacappelletti/nebula_config/"))
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              actorButton.text = path
              actorJsonPath = path
              println(path)
          }
        }

        val messageButton: Button = new Button("Select messages") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Messages Selected")
              val fileChooser = new FileChooser(new File("/Users/andreacappelletti/nebula_config/"))
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              messageButton.text = path
              messagesJsonPath = path
              println(path)
          }
        }

        val orchestratorButton: Button = new Button("Select orchestrator") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Orchestrator Selected")
              val fileChooser = new FileChooser(new File("/Users/andreacappelletti/nebula_config/"))
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              orchestratorButton.text = path
              orchestratorJsonPath = path
              println(path)
          }
        }

        val monitoringButton: Button = new Button("Select monitoring options") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Monitoring options selected")
              val fileChooser = new FileChooser(new File("/Users/andreacappelletti/nebula_config/"))
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              monitoringButton.text = path
              monitoringJsonPath = path
              println(path)
          }
        }

        val clusteringButton: Button = new Button("Select clustering options") {
          reactions += {
            case event.ButtonClicked(_) =>
              println("Clustering options selected")
              val fileChooser = new FileChooser(new File("/Users/andreacappelletti/nebula_config/"))
              fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
              fileChooser.showOpenDialog(this)
              val file = fileChooser.selectedFile
              val path = file.getAbsolutePath
              clusteringButton.text = path
              clusteringJsonPath = path
              println(path)
          }
        }

        val startNebulaButton: Button = new Button("Start Nebula") {
          reactions += {
            case event.ButtonClicked(_) =>
              if (actorJsonPath.nonEmpty && messagesJsonPath.nonEmpty && orchestratorJsonPath.nonEmpty) {
                Nebula.Main.startNebula(actorJsonPath, messagesJsonPath, orchestratorJsonPath, clusteringJsonPath, monitoringJsonPath)
                if (!systemRunning) {
                  systemRunning = true
                  startNebulaButton.text = "Reconfigure Nebula"
                } else {
                  Nebula.Main.stopNebula()
                  Nebula.Main.startNebula(actorJsonPath, messagesJsonPath, orchestratorJsonPath, clusteringJsonPath, monitoringJsonPath)
                }
                println("Nebula started")
              }
              else {
                println("Please select all files")
              }
          }
        }

        val stopNebulaButton: Button = new Button("Stop Nebula") {
          reactions += {
            case event.ButtonClicked(_) =>
              Nebula.Main.stopNebula()
              systemRunning = false
              startNebulaButton.text = "Start Nebula"
          }
        }

        contents += actorButton
        contents += new Label("Messages:")
        contents += messageButton
        contents += new Label("Orchestrator:")
        contents += orchestratorButton
        contents += new Label("Monitoring:")
        contents += monitoringButton
        contents += new Label("Clustering:")
        contents += clusteringButton
        contents += new Label("Start:")
        contents += startNebulaButton
        contents += new Label("Stop:")
        contents += stopNebulaButton
      }
      contents = gridPanel
      size = new Dimension(700, 800)
      //pack()
      centerOnScreen()
      open()
    }
  }




