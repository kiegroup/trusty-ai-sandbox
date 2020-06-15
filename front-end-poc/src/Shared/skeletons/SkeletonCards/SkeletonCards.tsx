import React from "react";
import { Gallery, GalleryItem } from "@patternfly/react-core";
import "./SkeletonCards.scss";
import SkeletonInlineStripe from "../SkeletonInlineStripe";
import { v4 as uuid } from "uuid";

type SkeletonCardProps = {
  quantity: number;
};

const SkeletonCards = (props: SkeletonCardProps) => {
  const { quantity } = props;
  let cards = [];
  for (let i = 0; i < quantity; i++) {
    cards.push(
      <GalleryItem key={uuid()}>
        <div className="skeleton-cards__card">
          <SkeletonInlineStripe key={uuid()} customStyle={{ width: 250, height: 25, margin: "20px" }} />
          <SkeletonInlineStripe key={uuid()} customStyle={{ width: 180, height: 20, margin: "10px 20px" }} />
        </div>
      </GalleryItem>
    );
  }
  return (
    <Gallery className="skeleton-cards" hasGutter>
      {cards}
    </Gallery>
  );
};

export default SkeletonCards;
