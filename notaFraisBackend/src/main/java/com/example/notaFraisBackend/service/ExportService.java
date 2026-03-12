package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.entities.entity.Evaluation;
import com.example.notaFraisBackend.entities.entity.FaitMarquant;
import com.example.notaFraisBackend.entities.entity.ObjectifEvaluation;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class ExportService {

   /* private static final String[] EXCEL_HEADERS = {
            "ID", "Année", "Collaborateur", "Évaluateur", "Date Entretien",
            "Note Objectifs", "Note Tenue Poste", "Note Finale", "Statut",
            "Date Création", "Date Validation"
    };

    private static final String[] CSV_HEADERS = {
            "ID", "Année", "Collaborateur", "Évaluateur", "Date Entretien",
            "Note Objectifs", "Note Tenue Poste", "Note Finale", "Statut",
            "Nb Objectifs", "Nb Formations", "Date Création", "Date Validation"
    };

    // Export Excel
    public ByteArrayInputStream exportEvaluationsToExcel(List<Evaluation> evaluations) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Évaluations");

            // Style pour l'en-tête
            Font headerFont = (Font) workbook.createFont();
            headerFont.setStyle(Font.BOLD);
            headerFont.setColor(BaseColor.WHITE);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont((org.apache.poi.ss.usermodel.Font) headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Créer l'en-tête
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < EXCEL_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(EXCEL_HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // Style pour les données
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Style pour les dates
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(dataStyle);
            dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

            // Style pour les notes
            CellStyle noteStyle = workbook.createCellStyle();
            noteStyle.cloneStyleFrom(dataStyle);
            noteStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.0"));

            // Remplir les données
            int rowNum = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Evaluation eval : evaluations) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(eval.getId());
                row.createCell(1).setCellValue(eval.getAnnee());
                row.createCell(2).setCellValue(eval.getCollaborateur().getNomComplet());
                row.createCell(3).setCellValue(eval.getEvaluateur() != null ? eval.getEvaluateur().getNomComplet() : "N/A");

                Cell dateCell = row.createCell(4);
                if (eval.getDateEntretien() != null) {
                    dateCell.setCellValue(dateFormat.format(eval.getDateEntretien()));
                    dateCell.setCellStyle(dateStyle);
                } else {
                    dateCell.setCellValue("N/A");
                }

                Cell noteObjCell = row.createCell(5);
                noteObjCell.setCellValue(eval.getNoteGlobaleObjectifs() != null ? eval.getNoteGlobaleObjectifs() : 0);
                noteObjCell.setCellStyle(noteStyle);

                Cell noteTenueCell = row.createCell(6);
                noteTenueCell.setCellValue(eval.getNoteGlobaleTenuePoste() != null ? eval.getNoteGlobaleTenuePoste() : 0);
                noteTenueCell.setCellStyle(noteStyle);

                Cell noteFinaleCell = row.createCell(7);
                noteFinaleCell.setCellValue(eval.getNoteGlobaleFinale() != null ? eval.getNoteGlobaleFinale() : 0);
                noteFinaleCell.setCellStyle(noteStyle);

                row.createCell(8).setCellValue(eval.getStatut() != null ? eval.getStatut().toString() : "BROUILLON");

                Cell creationCell = row.createCell(9);
                if (eval.getDateCreation() != null) {
                    creationCell.setCellValue(dateFormat.format(eval.getDateCreation()));
                    creationCell.setCellStyle(dateStyle);
                } else {
                    creationCell.setCellValue("N/A");
                }

                Cell validationCell = row.createCell(10);
                if (eval.getDateValidation() != null) {
                    validationCell.setCellValue(dateFormat.format(eval.getDateValidation()));
                    validationCell.setCellStyle(dateStyle);
                } else {
                    validationCell.setCellValue("N/A");
                }
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < EXCEL_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Export PDF
    public ByteArrayInputStream exportEvaluationsToPdf(List<Evaluation> evaluations) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Titre
        Font titleFont = (Font) FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Liste des évaluations", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Date d'export
        Font dateFont = (Font) FontFactory.getFont(FontFactory.HELVETICA, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Paragraph date = new Paragraph("Exporté le : " + sdf.format(new java.util.Date()), dateFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        date.setSpacingAfter(20);
        document.add(date);

        // Créer le tableau
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Largeurs des colonnes
        float[] columnWidths = {1f, 1.5f, 3f, 3f, 2f, 1.5f, 1.5f, 1.5f, 2f};
        table.setWidths(columnWidths);

        // Style pour l'en-tête
        Font headerFont = (Font) FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(41, 128, 185); // Bleu

        // En-têtes
        String[] headers = {"ID", "Année", "Collaborateur", "Évaluateur", "Date", "Obj.", "Tenue", "Finale", "Statut"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Style pour les données
        Font dataFont = (Font) FontFactory.getFont(FontFactory.HELVETICA, 9);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Remplir les données
        for (Evaluation eval : evaluations) {
            table.addCell(createCell(String.valueOf(eval.getId()), dataFont));
            table.addCell(createCell(String.valueOf(eval.getAnnee()), dataFont));
            table.addCell(createCell(eval.getCollaborateur().getNomComplet(), dataFont));
            table.addCell(createCell(eval.getEvaluateur() != null ? eval.getEvaluateur().getNomComplet() : "N/A", dataFont));
            table.addCell(createCell(eval.getDateEntretien() != null ? dateFormat.format(eval.getDateEntretien()) : "N/A", dataFont));

            // Notes avec formatage
            table.addCell(createCell(formatNote(eval.getNoteGlobaleObjectifs()), dataFont));
            table.addCell(createCell(formatNote(eval.getNoteGlobaleTenuePoste()), dataFont));
            table.addCell(createCell(formatNote(eval.getNoteGlobaleFinale()), dataFont));

            String statut = eval.getStatut() != null ? eval.getStatut().toString() : "BROUILLON";
            PdfPCell statutCell = createCell(statut, dataFont);
            statutCell.setBackgroundColor(getStatutColor(statut));
            table.addCell(statutCell);
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        // Résumé
        document.add(createSummary(evaluations));

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Export PDF d'une seule évaluation
    public ByteArrayInputStream exportSingleEvaluationToPdf(Evaluation eval) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // En-tête
        Font titleFont = (Font) FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("FICHE D'ÉVALUATION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Informations générales
        document.add(createInfoSection(eval));

        // Faits marquants
        document.add(createFaitsMarquantsSection(eval));

        // Objectifs
        document.add(createObjectifsSection(eval));

        // Tenue du poste
        document.add(createTenuePosteSection(eval));

        // Maîtrise
        document.add(createMaitriseSection(eval));

        // Notes
        document.add(createNotesSection(eval));

        // Signatures
        document.add(createSignaturesSection(eval));

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Export CSV
    public ByteArrayInputStream exportEvaluationsToCsv(List<Evaluation> evaluations) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Écrire l'en-tête
        writer.println(String.join(",", CSV_HEADERS));

        // Écrire les données
        for (Evaluation eval : evaluations) {
            StringBuilder line = new StringBuilder();
            line.append(eval.getId()).append(",");
            line.append(eval.getAnnee()).append(",");
            line.append("\"").append(eval.getCollaborateur().getNomComplet()).append("\",");
            line.append("\"").append(eval.getEvaluateur() != null ? eval.getEvaluateur().getNomComplet() : "N/A").append("\",");
            line.append(eval.getDateEntretien() != null ? dateFormat.format(eval.getDateEntretien()) : "N/A").append(",");
            line.append(eval.getNoteGlobaleObjectifs() != null ? eval.getNoteGlobaleObjectifs() : 0).append(",");
            line.append(eval.getNoteGlobaleTenuePoste() != null ? eval.getNoteGlobaleTenuePoste() : 0).append(",");
            line.append(eval.getNoteGlobaleFinale() != null ? eval.getNoteGlobaleFinale() : 0).append(",");
            line.append(eval.getStatut() != null ? eval.getStatut().toString() : "BROUILLON").append(",");
            line.append(eval.getObjectifs() != null ? eval.getObjectifs().size() : 0).append(",");
            line.append(eval.getSouhaitsFormations() != null ? eval.getSouhaitsFormations().size() : 0).append(",");
            line.append(eval.getDateCreation() != null ? dateFormat.format(eval.getDateCreation()) : "N/A").append(",");
            line.append(eval.getDateValidation() != null ? dateFormat.format(eval.getDateValidation()) : "N/A");
            writer.println(line.toString());
        }

        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Méthodes utilitaires
    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private String formatNote(Double note) {
        return note != null ? String.format("%.1f", note) : "-";
    }

    private BaseColor getStatutColor(String statut) {
        switch (statut) {
            case "VALIDEE": return new BaseColor(46, 204, 113); // Vert
            case "BROUILLON": return new BaseColor(241, 196, 15); // Jaune
            case "EN_COURS": return new BaseColor(52, 152, 219); // Bleu
            case "REFUSEE": return new BaseColor(231, 76, 60); // Rouge
            default: return BaseColor.LIGHT_GRAY;
        }
    }

    private Paragraph createSummary(List<Evaluation> evaluations) {
        Font summaryFont = (Font) FontFactory.getFont(FontFactory.HELVETICA, 11);
        Paragraph summary = new Paragraph("RÉSUMÉ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        summary.setSpacingBefore(10);

        long total = evaluations.size();
        long validees = evaluations.stream().filter(e -> e.getStatut() != null && e.getStatut().toString().equals("VALIDEE")).count();
        long enCours = evaluations.stream().filter(e -> e.getStatut() != null && e.getStatut().toString().equals("EN_COURS")).count();
        long brouillons = evaluations.stream().filter(e -> e.getStatut() == null || e.getStatut().toString().equals("BROUILLON")).count();

        summary.add(new Paragraph("Total des évaluations : " + total, summaryFont));
        summary.add(new Paragraph("Évaluations validées : " + validees, summaryFont));
        summary.add(new Paragraph("Évaluations en cours : " + enCours, summaryFont));
        summary.add(new Paragraph("Brouillons : " + brouillons, summaryFont));

        return summary;
    }

    private Paragraph createInfoSection(Evaluation eval) {
        Paragraph info = new Paragraph("INFORMATIONS GÉNÉRALES",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        info.setSpacingBefore(15);
        info.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        info.add(new Paragraph("Collaborateur : " + eval.getCollaborateur().getNomComplet(), normalFont));
        info.add(new Paragraph("Évaluateur : " + (eval.getEvaluateur() != null ? eval.getEvaluateur().getNomComplet() : "N/A"), normalFont));
        info.add(new Paragraph("Année : " + eval.getAnnee(), normalFont));
        info.add(new Paragraph("Date d'entretien : " + (eval.getDateEntretien() != null ? df.format(eval.getDateEntretien()) : "N/A"), normalFont));
        info.add(new Paragraph("Statut : " + (eval.getStatut() != null ? eval.getStatut().toString() : "BROUILLON"), normalFont));

        return info;
    }

    private Paragraph createFaitsMarquantsSection(Evaluation eval) {
        Paragraph section = new Paragraph("FAITS MARQUANTS",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        if (eval.getFaitsMarquants() != null && !eval.getFaitsMarquants().isEmpty()) {
            for (FaitMarquant fait : eval.getFaitsMarquants()) {
                section.add(new Paragraph("• " + fait, normalFont));
            }
        } else {
            section.add(new Paragraph("Aucun fait marquant renseigné", normalFont));
        }

        return section;
    }

    private Paragraph createObjectifsSection(Evaluation eval) {
        Paragraph section = new Paragraph("OBJECTIFS",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        if (eval.getObjectifs() != null && !eval.getObjectifs().isEmpty()) {
            for (ObjectifEvaluation obj : eval.getObjectifs()) {
                section.add(new Paragraph("• " + obj.getLibelle(), normalFont));
                section.add(new Paragraph("  Taux d'atteinte : " + (obj.getTauxAtteinte() != null ? obj.getTauxAtteinte() + "%" : "N/A"), normalFont));
                section.add(new Paragraph("  Cotation : " + (obj.getCotation() != null ? obj.getCotation() + "/10" : "N/A"), normalFont));
                section.add(new Paragraph("  "));
            }
        } else {
            section.add(new Paragraph("Aucun objectif renseigné", normalFont));
        }

        return section;
    }

    private Paragraph createTenuePosteSection(Evaluation eval) {
        Paragraph section = new Paragraph("TENUE DU POSTE",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        section.add(new Paragraph("Respect des engagements : " + (eval.getRespectEngagements() != null ? eval.getRespectEngagements() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Qualité méthodes de travail : " + (eval.getQualiteMethodesTravail() != null ? eval.getQualiteMethodesTravail() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Adaptation et organisation : " + (eval.getCapacitesAdaptationOrganisation() != null ? eval.getCapacitesAdaptationOrganisation() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Encadrement : " + (eval.getEncadrement() != null ? eval.getEncadrement() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Initiative et innovation : " + (eval.getEspritInitiativeInnovation() != null ? eval.getEspritInitiativeInnovation() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Relation présentation : " + (eval.getRelationPresentation() != null ? eval.getRelationPresentation() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Ponctualité : " + (eval.getPonctualite() != null ? eval.getPonctualite() + "/10" : "N/A"), normalFont));
        section.add(new Paragraph("Respect règlement : " + (eval.getRespectReglementInterieur() != null ? eval.getRespectReglementInterieur() + "/10" : "N/A"), normalFont));

        return section;
    }

    private Paragraph createMaitriseSection(Evaluation eval) {
        Paragraph section = new Paragraph("MAÎTRISE DU POSTE",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        section.add(new Paragraph("Niveau technique : " + (eval.getNiveauTechnique() != null ? eval.getNiveauTechnique().toString() : "N/A"), normalFont));
        section.add(new Paragraph("Niveau expérience : " + (eval.getNiveauExperience() != null ? eval.getNiveauExperience().toString() : "N/A"), normalFont));
        section.add(new Paragraph("Niveau encadrement : " + (eval.getNiveauEncadrement() != null ? eval.getNiveauEncadrement().toString() : "N/A"), normalFont));

        if (eval.getCommentairesMaitrise() != null && !eval.getCommentairesMaitrise().isEmpty()) {
            section.add(new Paragraph("Commentaires : " + eval.getCommentairesMaitrise(), normalFont));
        }

        return section;
    }

    private Paragraph createNotesSection(Evaluation eval) {
        Paragraph section = new Paragraph("NOTES",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        section.add(new Paragraph("Note des objectifs : " + formatNote(eval.getNoteGlobaleObjectifs()) + "/10", normalFont));
        section.add(new Paragraph("Note de tenue du poste : " + formatNote(eval.getNoteGlobaleTenuePoste()) + "/10", normalFont));
        section.add(new Paragraph("NOTE FINALE : " + formatNote(eval.getNoteGlobaleFinale()) + "/10",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        return section;
    }

    private Paragraph createSignaturesSection(Evaluation eval) {
        Paragraph section = new Paragraph("SIGNATURES",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);

        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        section.add(new Paragraph("Signature responsable : " + (eval.getSignatureResponsable() != null ? eval.getSignatureResponsable() : "En attente"), normalFont));
        section.add(new Paragraph("Signature collaborateur : " + (eval.getSignatureCollaborateur() != null ? eval.getSignatureCollaborateur() : "En attente"), normalFont));

        return section;
    }*/


}
