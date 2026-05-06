package com.fst.elearning.panier;

import com.fst.elearning.entity.Cours;
import com.fst.elearning.service.CoursService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PanierService {
    public static final String SESSION_KEY = "PANIER_ITEMS";

    private final CoursService coursService;

    public PanierService(CoursService coursService) {
        this.coursService = coursService;
    }

    @SuppressWarnings("unchecked")
    public List<PanierItem> getItems(HttpSession session) {
        Object existing = session.getAttribute(SESSION_KEY);
        if (existing instanceof List<?> list) {
            return (List<PanierItem>) list;
        }
        List<PanierItem> items = new ArrayList<>();
        session.setAttribute(SESSION_KEY, items);
        return items;
    }

    public void addCours(HttpSession session, Long coursId) {
        List<PanierItem> items = getItems(session);
        Optional<PanierItem> already = items.stream().filter(i -> i.getCoursId().equals(coursId)).findFirst();
        if (already.isPresent()) return;

        Cours cours = coursService.findById(coursId);
        items.add(PanierItem.builder()
                .coursId(cours.getId())
                .titre(cours.getTitre())
                .prix(cours.getPrix() != null ? cours.getPrix() : BigDecimal.ZERO)
                .imageUrl(cours.getImageUrl())
                .build());
    }

    public void removeCours(HttpSession session, Long coursId) {
        List<PanierItem> items = getItems(session);
        items.removeIf(i -> i.getCoursId().equals(coursId));
    }

    public void clear(HttpSession session) {
        session.setAttribute(SESSION_KEY, new ArrayList<PanierItem>());
    }

    public int count(HttpSession session) {
        return getItems(session).size();
    }

    public BigDecimal total(HttpSession session) {
        return getItems(session).stream()
                .map(i -> i.getPrix() != null ? i.getPrix() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

