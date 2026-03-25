//package io.github.kawazaki42.course.boolfnmin
//
//import javafx.fxml.FXML
//import javafx.scene.control.Label
//import javafx.scene.control.TextField
//import javafx.scene.image.ImageView
//import javafx.scene.layout.VBox
//import java.awt.Color
//
//class Controller {
//    @FXML private lateinit var cdnfView: ImageView
//    @FXML private lateinit var mdnfView: ImageView
//
//    @FXML private lateinit var cdnfLabel: Label
//    @FXML private lateinit var mdnfLabel: Label
//
//    @FXML private lateinit var inputField: TextField
//
//    @FXML private lateinit var root: VBox
//
//    @FXML
//    private fun initialize() {
//        cdnfView.fitWidthProperty().bind(root.widthProperty())
//        mdnfView.fitWidthProperty().bind(root.widthProperty())
//
//        displayFormula(cdnfView, "f = ...")
//        displayFormula(mdnfView, "f = ...")
//    }
//
//    private fun displayFormula(vw: ImageView, formula: String, c: Color? = null) {
//        val tf = TeXFormula(formula)
//
//        val swingImage = tf.createBufferedImage(0, 100, c, null)
//
//        val fxImage = SwingFXUtils.toFXImage(swingImage, null)
//
//        vw.image = fxImage
//    }
//
//    @FXML
//    private fun onButtonPress() {
//        val input = inputField.text
//        val col = BooleanFunction.parse(input) ?: return
//        val f = BooleanFunction(col)
//
//        if (f == null) {
//            cdnfLabel.text = "TODO"
//            mdnfLabel.text = ""
//
//            displayFormula(cdnfView, "f = ...", Color.RED)
//            displayFormula(mdnfView, "f = ...", Color.RED)
//
//            return
//        }
//
//        if (f.isConstantFalse()) {
//            displayFormula(cdnfView, "f = ...", Color.RED)
//            displayFormula(mdnfView, "f = ...", Color.RED)
//
//            cdnfLabel.text = "СДНФ не существует!"
//            mdnfLabel.text = "МДНФ не существует!"
//
//            return
//        }
//
//        cdnfLabel.text = "СДНФ:"
//        mdnfLabel.text = "МДНФ:"
//
//        val cdnf = f.cdnf.toLatex()
//        val mdnf = f.minifiedLatexString
//
//        displayFormula(cdnfView, "f = $cdnf")
//        displayFormula(mdnfView, "f = $mdnf")
//
//        // debug
//        println("---")
//        println("$input:")
//        println(f.formatImplicantTable())
//    }
//}