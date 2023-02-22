class recipeTile {
  final String? title;
  final String? ingredients; //we will join list of ingredients with .join(", ")
  final int? totalTime;
  final DateTime? lastDateMade;
  bool? isSelected;

  recipeTile(
      {this.isSelected,
        this.title,
        this.ingredients,
        this.totalTime,
        this.lastDateMade});
}