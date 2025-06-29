package dev.stone.practice.party;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum PartyPrivacy {

	OPEN("Open"),
	CLOSED("Close");

	private final String readable;

}
