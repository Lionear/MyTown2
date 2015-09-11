package mytown.api.container;

import mytown.entities.Town;
import mytown.entities.flag.Flag;
import mytown.entities.flag.ProtectionFlagType;
import myessentials.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class FlagsContainer extends ArrayList<Flag> {

    public boolean contains(ProtectionFlagType type) {
        for (Flag flag : this) {
            if (flag.getFlagType() == type) {
                return true;
            }
        }
        return false;
    }

    public Flag get(ProtectionFlagType type) {
        for (Flag flag : this)
            if (flag.getFlagType() == type)
                return flag;
        return null;
    }

    public void remove(ProtectionFlagType type) {
        for (Iterator<Flag> it = iterator(); it.hasNext(); ) {
            if (it.next().getFlagType() == type) {
                it.remove();
            }
        }
    }

    public Object getValue(ProtectionFlagType type) {
        for (Flag flag : this) {
            if (flag.getFlagType() == type)
                return flag.getValue();
        }
        return type.getDefaultValue();
    }

    public String toStringForTowns() {
        String formattedFlagList = "";

        for (Flag flag : this) {
            if(flag.getFlagType().canTownsModify()) {
                if (!formattedFlagList.equals("")) {
                    formattedFlagList += "\\n";
                }
                formattedFlagList += flag.toString();
            }
        }

        String unconfigurableFlags = "";
        for(ProtectionFlagType flagType : ProtectionFlagType.values()) {
            if(!contains(flagType)) {
                unconfigurableFlags += "\\n" + (new Flag(flagType, flagType.getDefaultValue())).toString(ColorUtils.colorValueConst);
            }
        }

        formattedFlagList += unconfigurableFlags;

        return formattedFlagList;
    }

    public String toStringForPlot(Town town) {
        String formattedFlagList = "";

        for (Flag flag : this) {
            if(flag.getFlagType().canTownsModify()) {
                if (!formattedFlagList.equals("")) {
                    formattedFlagList += "\\n";
                }
                formattedFlagList += flag.toString();
            }
        }

        String unconfigurableFlags = "";
        for(ProtectionFlagType flagType : ProtectionFlagType.values()) {
            if(!contains(flagType)) {
                unconfigurableFlags += "\\n" + (new Flag(flagType, town.flagsContainer.getValue(flagType))).toString(ColorUtils.colorValueConst);
            }
        }

        formattedFlagList += unconfigurableFlags;

        return formattedFlagList;
    }

    public String toStringForWild() {
        String formattedFlagList = "";

        for (Flag flag : this) {
            if (!formattedFlagList.equals("")) {
                formattedFlagList += "\\n";
            }
            formattedFlagList += flag.toString();
        }

        return formattedFlagList;
    }
}
