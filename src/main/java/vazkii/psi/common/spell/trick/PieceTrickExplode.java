/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [18/01/2016, 22:18:31 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickExplode extends PieceTrick {

	SpellParam position;
	SpellParam power;
	
	public PieceTrickExplode(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, true));
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		double powerVal = this.<Double>getParamEvaluation(power);
		if(powerVal <= 0)
			throw new SpellCompilationException("nonpositivevalue", x, y);
		
		meta.addStat(EnumSpellStat.POTENCY, (int) (powerVal * 70));
		meta.addStat(EnumSpellStat.COST, (int) (powerVal * 70));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.worldObj.isRemote)
			return null;
			
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);
		Double powerVal = this.<Double>getParamValue(context, power);
		
		if(positionVal == null)
			throw new SpellRuntimeException("nullvector");
		
		context.caster.worldObj.createExplosion(context.focalPoint, positionVal.x, positionVal.y, positionVal.z, powerVal.floatValue(), true);
		
		return null;
	}

}
