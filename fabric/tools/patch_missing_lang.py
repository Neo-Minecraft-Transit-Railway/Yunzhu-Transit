#!/usr/bin/env python3
"""Add missing block.yte.* / item.yte.* keys to lang files."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LANG_DIR = ROOT / "src/main/resources/assets/yte/lang"
JAVA_DIR = ROOT / "src/main/java/top/xfunny/mod"


def collect_ids() -> set[str]:
	ids: set[str] = set()
	pattern = re.compile(r'new Identifier\(Init\.MOD_ID, "([^"]+)"\)')
	for name in ("Blocks.java", "Items.java"):
		text = (JAVA_DIR / name).read_text(encoding="utf-8")
		ids.update(pattern.findall(text))
	return ids


def fallback_name(block_id: str, lang: dict) -> str:
	block_key = f"block.yte.{block_id}"
	item_key = f"item.yte.{block_id}"
	if block_key in lang:
		return lang[block_key]
	if item_key in lang:
		return lang[item_key]

	rs01 = re.match(r"pat_rs01_railway_sign_(\d+)_(even|odd)$", block_id)
	if rs01:
		n, parity = rs01.groups()
		parity_zh = "偶数" if parity == "even" else "奇数"
		return f"PAT RS01指示牌（{n}，{parity_zh}）"
	if block_id == "pat_rs01_railway_sign_middle":
		return "PAT RS01指示牌（中间段）"

	for prefix in sorted(
		(k.removeprefix("block.yte.") for k in lang if k.startswith("block.yte.")),
		key=len,
		reverse=True,
	):
		if block_id.startswith(prefix + "_") or block_id.startswith(prefix):
			suffix = block_id[len(prefix) :].lstrip("_")
			base = lang.get(f"block.yte.{prefix}", block_id)
			if not suffix:
				return base
			suffix = suffix.replace("_", " ")
			return f"{base}（{suffix}）"

	return block_id.replace("_", " ")


def main() -> None:
	ids = collect_ids()
	for lang_file in ("zh_cn.json", "zh_hk.json", "en_us.json"):
		path = LANG_DIR / lang_file
		lang = json.loads(path.read_text(encoding="utf-8"))
		added = 0
		for block_id in sorted(ids):
			block_key = f"block.yte.{block_id}"
			item_key = f"item.yte.{block_id}"
			if block_key in lang or item_key in lang:
				continue
			name = fallback_name(block_id, lang)
			lang[block_key] = name
			added += 1
		path.write_text(json.dumps(lang, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
		print(f"{lang_file}: added {added} keys")


if __name__ == "__main__":
	main()
