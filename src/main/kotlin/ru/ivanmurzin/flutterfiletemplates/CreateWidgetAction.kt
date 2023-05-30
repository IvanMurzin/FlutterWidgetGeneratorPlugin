package ru.ivanmurzin.flutterfiletemplates

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.PlatformIcons
import java.util.*


class CreateWidgetAction : CreateFileFromTemplateAction("Widget", "Creates new Flutter widget", PlatformIcons.VARIABLE_WRITE_ACCESS) {

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New Widget")
                .addKind("StatelessWidget", PlatformIcons.CLOSED_MODULE_GROUP_ICON, "StatelessWidget")
                .addKind("StatefulWidget", PlatformIcons.SYNCHRONIZE_ICON, "StatefulWidget")
                .addKind("ConsumerWidget", PlatformIcons.VARIABLE_ICON, "ConsumerWidget")
                .addKind("ConsumerStatefulWidget", PlatformIcons.VARIABLE_RW_ACCESS, "ConsumerStatefulWidget");
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "Create widget: $newName"
    }

    override fun createFileFromTemplate(name: String?, template: FileTemplate?, dir: PsiDirectory?): PsiFile {
        val properties = Properties()
        if (name != null) {
            if (name.contains("_")) {
                val nameParts = name.split("_");
                val widgetName = StringBuffer()
                nameParts.forEach { p ->
                    widgetName.append(p.replaceFirstChar(Char::titlecase))
                }
                properties.setProperty("WIDGETNAME", widgetName.toString())
                return FileTemplateUtil.createFromTemplate(template!!, name, properties, dir!!) as PsiFile
            }
            val fileName = StringBuffer()
            val fileNamePart = StringBuffer()
            var words = 0
            for (c in name) {
                if (c == c.titlecaseChar()) {
                    fileName.append(fileNamePart)
                    ++words
                    fileNamePart.delete(0, fileNamePart.length)
                    if (fileName.isNotEmpty()) fileNamePart.append("_")
                    fileNamePart.append(c.lowercase())
                } else fileNamePart.append(c)
            }
            fileName.append(fileNamePart)
            if (words != 0) properties.setProperty("WIDGETNAME", name)
            else properties.setProperty("WIDGETNAME", name.replaceFirstChar(Char::titlecase))
            return FileTemplateUtil.createFromTemplate(template!!, fileName.toString(), properties, dir!!) as PsiFile
        }
        return super.createFileFromTemplate(null, template, dir)
    }
}
