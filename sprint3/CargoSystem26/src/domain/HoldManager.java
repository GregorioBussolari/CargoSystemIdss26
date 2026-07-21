package domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.HoldManifestReader;

public class HoldManager implements IHoldManager{
    
    private Map<String, Slot> slots = new LinkedHashMap<>();
    
    // Proprietà dedicata esclusivamente all'area speciale di Labeling
    private Slot slot5;

    /**
     * Costruttore di Produzione (usato nel QActor CargoService)
     * @param manifestPath Percorso del file di configurazione
     */
    public HoldManager(String manifestPath) {
        // Usa il lettore condiviso per estrarre SOLO i nomi degli slot dinamici
        List<String> slotNames = HoldManifestReader.extractSlotNames(HoldManifestReader.read(manifestPath));
        initSlots(slotNames);
    }

    /**
     * Costruttore per i Test Unitari (Garantisce la retrocompatibilità)
     * Permette di istanziare la Hold iniettando semplicemente una lista di nomi.
     * @param slotNames Lista dei nomi degli slot (es. List.of("slot1", "slot2"))
     */
    public HoldManager(List<String> slotNames) {
        initSlots(slotNames);
    }

    private void initSlots(List<String> slotNames) {
        for (String name : slotNames) {
        	if(!name.equalsIgnoreCase("slot5"))
        		this.slots.put(name, new Slot(name));
        	else
        		this.slot5 = new Slot(name);
        }
    }

    /**
     * Verifica se tutti gli slot dinamici (1-4) sono occupati.
     */
    public boolean isFull() {
        if (slots.isEmpty()) {
            return false;
        }
        return slots.values().stream().allMatch(Slot::isOccupato);
    }

    /**
     * Cerca e restituisce il primo slot logico libero.
     */
    public ISlot findFreeSlot() {
        return slots.values().stream()
                .filter(slot -> !slot.isOccupato())
                .findFirst()
                .orElse(null);
    }

    /**
     * Recupera un oggetto Slot logico partendo dal suo nome.
     */
    public Slot getSlot(String name) {
        return slots.get(name);
    }

    /**
     * Svuota tutti gli slot correntemente registrati.
     */
    public void clearAllSlots() {
        slots.values().forEach(Slot::removeContainer);
    }
    
    public void addSlot(String name, Slot slot) {
        if (name != null && slot != null) {
            this.slots.put(name, slot);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== HOLD STATUS (LOGICAL) ===\n");
        sb.append("Stato Stiva: ").append(isFull() ? "COMPLETAMENTE PIENA" : "POSTI DISPONIBILI").append("\n");
        sb.append("-----------------------------\n");
        if (slots.isEmpty()) {
            sb.append(" Nessuno slot configurato.\n");
        } else {
            slots.values().forEach(slot -> {
                String stato = slot.isOccupato() ? "[ OCCUPATO ]" : "[  LIBERO  ]";
                sb.append(String.format(" -> %-7s Stato: %s\n", slot.getName(), stato));
            });
        }
        sb.append("=============================");
        return sb.toString();
    }
	
	
}
