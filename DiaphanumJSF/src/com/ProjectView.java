package com;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.boxable.BaseTable;
import com.boxable.Cell;
import com.boxable.HorizontalAlignment;
import com.boxable.Row;
import com.boxable.VerticalAlignment;
import com.model.Project;
import com.model.ProjectMember;
import com.model.User;

public class ProjectView implements Serializable {
    
    /**
     * @author ttt
     */
    private static final long serialVersionUID = 1L;
    
    private ExternalContext externalContext = FacesContext.getCurrentInstance()
	    .getExternalContext();
    private String imgRelativeWebPath = "/resources/img/";
    private String fontRelativeWebPath = "/resources/font/";
    private ServletContext servletContext = (ServletContext) externalContext
	    .getContext();
    private String imgAbsolutePath = servletContext
	    .getRealPath(imgRelativeWebPath);
    private String fontAbsolutePath = servletContext
	    .getRealPath(fontRelativeWebPath);
    private static final String filesDirectory = "/diaphanum/files/";
    private String filename;
    private Integer currentRow = 0;
    private Float projectNameHeight;
    private Float margin = 50f;
    private Float width;
    private Boolean drawLines = false;
    private Boolean drawContent = true;
    private Color colorDoc = new Color(126, 151, 173);
    private Float yStartNewPage = 0f;
    private Float yStart = yStartNewPage;
    private HorizontalAlignment align = HorizontalAlignment.RIGHT;
    private VerticalAlignment valign = VerticalAlignment.MIDDLE;
    private Float logoHeight = 100f;
    private Float titleRowHeight = 18f;
    private Float titleFondSize = 15f;
    private Float rowHeight = 15f;
    private Integer rowsPerPage = 47;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ResourceBundle bundle = context.getApplication().getResourceBundle(
	    context, "msgs");

    public ProjectView() {
	@SuppressWarnings("unused")
	LogBean log = new LogBean(
		"ERROR ProjectView.ProjectView(): can't create empty project view: ");
    }

    public ProjectView(Project project, User owner) {
	filename = filesDirectory.concat("project".concat(project.getId()
		.toString().concat(".pdf")));
	projectNameHeight = Float.valueOf(String.valueOf(titleRowHeight
		* Math.ceil(project.getName().length() / (float) 29)));
    }

    public ProjectView(Project project, User owner, List<ProjectMember> members) {
	this(project, owner);
	addLogo(project);
	addProjectName(project);
	addProjectOwner(project, owner);
	addProjectMembers(project, members);
	addProjectDescription(project);
	addProjectPurpose(project);
	addProjectTask(project);
	addProjectTargetGroup(project);
	addProjectSchedule(project);
	addProjectResources(project);
	addProjectBudjet(project);
	addProjectPartners(project);
    }

    private void addLogo(Project project) {
	try {
	    PDDocument document = new PDDocument();
	    PDPage page = new PDPage(PDRectangle.A4);
	    document.addPage(page);
	    PDPageContentStream stream = new PDPageContentStream(document, page);
	    PDImageXObject ximage = PDImageXObject.createFromFile(
		    imgAbsolutePath.concat("/" + "projectPrintLogo.png"),
		    document);
	    width = page.getMediaBox().getWidth() - (2 * margin);
	    yStart = page.getMediaBox().getHeight() - (margin + logoHeight);
	    stream.drawImage(ximage, margin, yStart, width, logoHeight);
	    currentRow += 6;
	    stream.close();
	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addLogo("
		    + project.getId() + "): OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addLogo("
		    + project.getId() + "): " + e.getMessage());
	}
    }

    private void addProjectName(Project project) {
	String projectName = project.getName();
	List<String> projectNameList = new ArrayList<String>();
	while (projectName.length() > 30) {
	    projectNameList.add(projectName.substring(0, 29));
	    projectName = projectName.substring(29);
	}
	if (projectName.length() > 0) {
	    projectNameList.add(projectName);
	}

	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(0);

	    PDType0Font fontFreeSansBold = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSansBold.ttf")));
	    PDPageContentStream stream = new PDPageContentStream(document,
		    page, true, true);

	    stream.setNonStrokingColor(colorDoc);
	    yStart = page.getMediaBox().getHeight()
		    - (margin + logoHeight + projectNameHeight);
	    stream.addRect(margin, yStart - 3, width, projectNameHeight);
	    stream.fill();

	    stream.setFont(fontFreeSansBold, titleFondSize);
	    stream.setNonStrokingColor(Color.WHITE);
	    for (String projectNamePart : projectNameList) {
		yStart = page.getMediaBox().getHeight()
			- (margin + 10 + (titleRowHeight * currentRow));
		stream.beginText();
		stream.newLineAtOffset(55, yStart);
		stream.showText(projectNamePart.toUpperCase());
		stream.endText();
		currentRow++;
	    }
	    stream.close();

	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addName("
		    + project.getId() + ") OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addName("
		    + project.getId() + "), addContent: " + e.getMessage());
	}
    }

    private void addProjectOwner(Project project, User owner) {
	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(0);
	    PDPageContentStream stream = new PDPageContentStream(document,
		    page, true, true);

	    PDType0Font fontFreeSans = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSans.ttf")));
	    PDType0Font fontFreeSansBold = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSansBold.ttf")));

	    yStartNewPage = page.getMediaBox().getHeight()
		    - (margin + (titleRowHeight * currentRow));
	    yStart = yStartNewPage;
	    BaseTable table = new BaseTable(yStart, yStartNewPage, margin,
		    width, margin, document, page, drawLines, drawContent);

	    Row<PDPage> row = table.createRow(25f);
	    table.setHeader(row);

	    row = table.createRow(25f);
	    Cell<PDPage> cell = row.createCell(25, null, align, valign);
	    cell.setFont(fontFreeSansBold);
	    cell.setFontSize(12f);
	    cell.setTopPadding(12f);
	    cell.setTextColor(colorDoc);
	    cell.setText(bundle.getString("projectTeam").substring(0, 7)
		    .toUpperCase());

	    cell = row.createCell(75, null);
	    cell.setFont(fontFreeSans);
	    cell.setFontSize(12f);
	    cell.setTopPadding(12f);
	    cell.setTextColor(Color.BLACK);
	    cell.setText(bundle.getString("accountablePerson") + ": "
		    + owner.getFullname() + ",");
	    currentRow++;

	    row = table.createRow(25f);
	    cell = row.createCell(25, null, align, valign);
	    cell.setFont(fontFreeSansBold);
	    cell.setFontSize(12f);
	    cell.setTopPadding(12f);
	    cell.setTextColor(colorDoc);
	    cell.setText(bundle.getString("projectTeam").substring(8, 20)
		    .toUpperCase());

	    cell = row.createCell(75, null);
	    cell.setFont(fontFreeSans);
	    cell.setFontSize(12f);
	    cell.setTopPadding(12f);
	    cell.setTextColor(Color.BLACK);
	    cell.setText(bundle.getString("email") + ": " + owner.getEmail()
		    + ", " + bundle.getString("phoneReduced") + ": "
		    + owner.getPhone());
	    currentRow++;

	    table.draw();
	    stream.close();
	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addProjectOwner("
		    + project.getId() + ") OK");
	} catch (Exception e) {
	    System.err.println("ProjectView: " + e.getMessage());
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addProjectOwner("
		    + project.getId() + "), addContent: " + e.getMessage());
	}
    }

    private void addProjectMembers(Project project, List<ProjectMember> members) {
	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(0);

	    PDType0Font fontFreeSans = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSans.ttf")));
	    if (members.size() > 0) {
		currentRow -= 2;
	    } else {
		currentRow++;
	    }
	    for (ProjectMember member : members) {
		if (currentRow < rowsPerPage) {
		    yStartNewPage = page.getMediaBox().getHeight()
			    - (margin + projectNameHeight + 5 + (rowHeight * currentRow));
		    yStart = yStartNewPage;
		    PDPageContentStream stream = new PDPageContentStream(
			    document, page, true, true);
		    stream.setFont(fontFreeSans, 12);
		    stream.setNonStrokingColor(Color.BLACK);
		    stream.beginText();
		    stream.newLineAtOffset(margin + 130f, yStart);
		    stream.showText(member.getFullname());
		    stream.endText();
		    stream.close();
		} else {
		    int currentRowPage = currentRow % rowsPerPage;
		    if (currentRowPage == 0) {
			page = new PDPage(PDRectangle.A4);
			document.addPage(page);
		    }
		    page = document.getPage(document.getNumberOfPages() - 1);
		    yStartNewPage = page.getMediaBox().getHeight()
			    - (margin + 5 + (rowHeight * currentRowPage));
		    yStart = yStartNewPage;
		    PDPageContentStream stream = new PDPageContentStream(
			    document, page, true, true);
		    stream.setFont(fontFreeSans, 12);
		    stream.setNonStrokingColor(Color.BLACK);
		    stream.beginText();
		    stream.newLineAtOffset(margin + 130f, yStart);
		    stream.showText(member.getFullname());
		    stream.endText();
		    stream.close();
		}
		currentRow++;
	    }
	    currentRow++;
	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addProjectMembers("
		    + project.getId() + ") OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addProjectMembers("
		    + project.getId() + "), addContent: " + e.getMessage());
	}
    }

    private void addProjectDescription(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "description", project.getDescription());
    }

    private void addProjectPurpose(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "purposes", project.getPurpose());
    }

    private void addProjectTask(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "tasks", project.getPurpose());
    }

    private void addProjectTargetGroup(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "targetGroup", project.getTargetGroup());
    }

    private void addProjectSchedule(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "schedule", project.getSchedule());
    }

    private void addProjectResources(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "resources", project.getResourse());
    }

    private void addProjectBudjet(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "budjet", project.getBudjetText());

    }

    private void addProjectPartners(Project project) {
	addLine(project.getId());
	addTextField(project.getId(), "partners", project.getPartners());
    }

    private void addTextField(Integer projectId, String fieldName,
	    String fieldContent) {
	fieldContent = fieldContent.replaceAll("(\\r|\\n)", "");
	List<String> fieldContentList = new ArrayList<String>();
	while (fieldContent.length() > 37) {
	    fieldContentList.add(fieldContent.substring(0, 36));
	    fieldContent = fieldContent.substring(36);
	}
	if (fieldContent.length() > 0) {
	    fieldContentList.add(fieldContent);
	}

	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(document.getNumberOfPages() - 1);
	    PDPageContentStream stream = new PDPageContentStream(document,
		    page, true, true);

	    PDType0Font fontFreeSans = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSans.ttf")));
	    PDType0Font fontFreeSansBold = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSansBold.ttf")));

	    if (currentRow < rowsPerPage) {
		yStartNewPage = page.getMediaBox().getHeight()
			- (margin + (rowHeight * currentRow));
		yStart = yStartNewPage;
		BaseTable table = new BaseTable(yStart, yStartNewPage, margin,
			width, margin, document, page, drawLines, drawContent);

		Row<PDPage> row = table.createRow(25f);
		table.setHeader(row);

		row = table.createRow(25f);
		Cell<PDPage> cell = row.createCell(25, null, align, valign);
		cell.setFont(fontFreeSansBold);
		cell.setFontSize(12f);
		cell.setTopPadding(12f);
		cell.setTextColor(colorDoc);
		cell.setText(bundle.getString(fieldName).toUpperCase());

		cell = row.createCell(75, null);
		cell.setFont(fontFreeSans);
		cell.setFontSize(12f);
		cell.setTopPadding(12f);
		cell.setTextColor(Color.BLACK);
		if (fieldContentList.size() > 0) {
		    cell.setText(fieldContentList.get(0));
		    fieldContentList.remove(0);
		}
		table.draw();
	    } else {
		int currentRowPage = currentRow % rowsPerPage;
		if (currentRowPage == 0) {
		    page = new PDPage(PDRectangle.A4);
		    document.addPage(page);
		}
		page = document.getPage(document.getNumberOfPages() - 1);

		yStartNewPage = page.getMediaBox().getHeight()
			- (margin + (rowHeight * currentRowPage));
		yStart = yStartNewPage;
		BaseTable table = new BaseTable(yStart, yStartNewPage, margin,
			width, margin, document, page, drawLines, drawContent);

		Row<PDPage> row = table.createRow(25f);
		table.setHeader(row);
		row = table.createRow(25f);
		Cell<PDPage> cell = row.createCell(25, null, align, valign);
		cell.setFont(fontFreeSansBold);
		cell.setFontSize(12f);
		cell.setTopPadding(12f);
		cell.setTextColor(colorDoc);
		cell.setText(bundle.getString(fieldName).toUpperCase());
		cell = row.createCell(75, null);
		cell.setFont(fontFreeSans);
		cell.setFontSize(12f);
		cell.setTopPadding(12f);
		cell.setTextColor(Color.BLACK);
		if (fieldContentList.size() > 0) {
		    cell.setText(fieldContentList.get(0));
		    fieldContentList.remove(0);
		}
		table.draw();
	    }
	    currentRow++;
	    stream.close();
	    document.save(filename);
	    document.close();

	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addProject"
		    + WordUtils.capitalize(fieldName) + "(" + projectId
		    + "), addFirstLine OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addProject"
		    + WordUtils.capitalize(fieldName) + "(" + projectId
		    + "), addFirstLine: " + e.getMessage());
	}

	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(document.getNumberOfPages() - 1);

	    PDType0Font fontFreeSans = PDType0Font.load(document, new File(
		    fontAbsolutePath.concat("/" + "FreeSans.ttf")));

	    for (String string : fieldContentList) {
		if (currentRow < rowsPerPage) {
		    yStartNewPage = page.getMediaBox().getHeight()
			    - (margin + projectNameHeight + (rowHeight * currentRow));
		    yStart = yStartNewPage;
		    PDPageContentStream stream = new PDPageContentStream(
			    document, page, true, true);
		    stream.setFont(fontFreeSans, 12);
		    stream.setNonStrokingColor(Color.BLACK);
		    stream.beginText();
		    stream.newLineAtOffset(margin + 130f, yStart);
		    stream.showText(string);
		    stream.endText();
		    stream.close();
		} else {
		    int currentRowPage = currentRow % rowsPerPage;
		    if (currentRowPage == 0) {
			page = new PDPage(PDRectangle.A4);
			document.addPage(page);
		    }
		    page = document.getPage(document.getNumberOfPages() - 1);
		    yStartNewPage = page.getMediaBox().getHeight()
			    - (margin + 10 + (rowHeight * currentRowPage));
		    yStart = yStartNewPage;
		    PDPageContentStream stream = new PDPageContentStream(
			    document, page, true, true);
		    stream.setFont(fontFreeSans, 12);
		    stream.setNonStrokingColor(Color.BLACK);
		    stream.beginText();
		    stream.newLineAtOffset(margin + 130f, yStart);
		    stream.showText(string);
		    stream.endText();
		    stream.close();
		}
		currentRow++;
	    }

	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView.addProject"
		    + WordUtils.capitalize(fieldName) + "(" + projectId
		    + "), addNewLine OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView.addProject"
		    + WordUtils.capitalize(fieldName) + "(" + projectId
		    + "), addNewLine: " + e.getMessage());
	}
	currentRow++;
    }

    @SuppressWarnings("deprecation")
    private void addLine(Integer projectId) {
	try {
	    PDDocument document = PDDocument.load(new File(filename));
	    PDPage page = document.getPage(document.getNumberOfPages() - 1);
	    PDPageContentStream streamLine = new PDPageContentStream(document,
		    page, true, true);
	    streamLine.setStrokingColor(colorDoc);
	    streamLine.setLineWidth(1f);
	    streamLine.drawLine(margin, yStart - rowHeight, width + margin,
		    yStart - rowHeight);
	    streamLine.close();
	    document.save(filename);
	    document.close();
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("INFO ProjectView(" + projectId
		    + ").addLine OK");
	} catch (Exception e) {
	    @SuppressWarnings("unused")
	    LogBean log = new LogBean("ERROR ProjectView(" + projectId
		    + ").addLine OK");
	}
    }
}
